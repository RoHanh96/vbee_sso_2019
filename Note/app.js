var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var session = require('express-session');
var axios = require('axios');
var jwt = require('jsonwebtoken');
const JSON1 = require('circular-json');
const con = require('./constant');
const targetBaseUrl = con.ssoUrl + "/login";


var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var fibonacyRouter = require('./routes/fibonaci');
var notes = require('./routes/notes');


var app = express();
const signingKey = "signingKey";

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(session({secret: "hanhnv12345"}));
app.use(express.static(path.join(__dirname, 'public')));


app.use('*', handleRedirect);

app.use('/', indexRouter);
// app.use('/users', usersRouter);
app.use('/fibonaci', fibonacyRouter.index);
app.use('/noteadd', notes.add);
app.use('/notesave', notes.save);
app.use('/noteview', notes.view);
app.use('/noteedit', notes.edit);
app.use('/destroy', notes.destroy);
app.use('/notedodestroy', notes.dodestroy);
app.use('/logout', async function (req, res, next) {
  res.clearCookie("access-token");
  let checkLogout = "1";
  const options = {
    method: "post",
    url: con.ssoUrl + "/clearCookieJs",
    data: {
      checkLogout: checkLogout
    }
  };
  try {
    await axios(options);
  }catch (e) {
    console.log(e);
  }
  console.log("access-token" + req.cookies["access-token"] );
  res.redirect("/");
});

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  next(createError(404));
});

// error handler
app.use(function(err, req, res, next) {
  // set locals, only providing error in development
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // render the error page
  res.status(err.status || 500);
  res.render('error');
});


async function handleRedirect(req, res, next){
  console.log("token access" + req.cookies["access-token"]);
  if(!req.cookies["access-token"]){
    var token = undefined;
    var jwtToken = undefined;
    token = req.query.token;
    console.log("token"+ token);
    if(token){
      const options = {
        method: 'get',
        url: con.ssoUrl + "/getJwtToken",
        params: {token}
      };
      const results = await axios(options);
      jwtToken = results.data;
    }
    console.log("jwt token" + jwtToken);
    if(jwtToken){
      var tokenRefresh = jwtToken;
      const  options = {
        method: 'post',
        url: con.ssoUrl + "/setJwtToken",
        data: {
          tokenRefresh: tokenRefresh
        }
      };
      await axios(options);
      res.cookie('access-token', jwtToken);
      req.session.access_token = jwtToken;
      const options1 = {
        method: 'get',
        url: con.ssoUrl + "/getUserInfo",
        params: {jwtToken}
      };
      const userInfoJson = await axios(options1);
      console.log("userJson" + JSON1.stringify(userInfoJson.data));
      //const decode = jwt.verify(jwtToken, "signingKey");
      // const user = JSON.parse(userInfoJson.data);
      // console.log(user.username);
      req.session.username = userInfoJson.data.username;
      res.locals.username = userInfoJson.data.username;
      console.log("dang nhap thanh cong");
      next();
    }
    else{
      var fullUrl = req.protocol + '://' + req.get('host') + req.originalUrl;
      const  targetUrl = targetBaseUrl + "?callbackUrl=" + fullUrl;
      console.log("dang nhap lan dau");
      res.redirect(targetUrl);
    }
  }
  else{
    if(req.cookies["access-token"] === req.session.access_token){
      res.locals.username =  req.session.username;
      console.log("dang nhap lan thu 2");
      next();
    }
  }

}

module.exports = app;
