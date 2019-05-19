var notes = require('../models/notes');
exports.add = function (req, res, next) {
    res.render('noteedit', {
        title: 'Add a note',
        doccreate: true,
        notekey: "",
        note: undefined
    });
}
exports.save = function (req, res, next) {
    if(req.body.doccreate === 'create'){
        notes.create(req.body.notekey, req.body.title, req.body.body);
    }
    else{
        notes.update(req.body.notekey, req.body.title, req.body.body)
    }
    res.redirect('/noteview?key='+req.body.notekey);
}

exports.view = function (req, res, next) {
    var note = undefined;
    if(req.query.key){
        note = notes.read(req.query.key);
    }
    res.render('noteview', {
        title: note? note.title: "",
        notekey: req.query.key,
        note: note
    });
}

exports.edit = function (req, res, next) {
    var  note = undefined;
    if(req.query.key){
        note = notes.read(req.query.key);
    }
    res.render('noteedit',{
        title: note? ("Edit " + note.title) : "Add a note",
        doccreate: note? false : true,
        notekey: req.query.key,
        note: note
    });
}

exports.destroy = function (req, res, next) {
    var note = undefined;
    if(req.query.key){
        note = notes.read(req.query.key);
    }
    res.render('notedestroy', {
        title: note? note.title : "",
        notekey: req.query.key,
        note: note
    });
}

exports.dodestroy = function (req, res, next) {
    notes.destroy(req.body.notekey);
    res.redirect('/');
}