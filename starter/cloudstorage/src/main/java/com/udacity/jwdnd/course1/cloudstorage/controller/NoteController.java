package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

//    @PostMapping("/addNote")
//    public String addNote(@ModelAttribute("note") Note note, Authentication authentication, Model model) {
//        Integer userId = userService.getUserId(authentication.getName());
//        boolean rs = false;
//        if (userId != null) {
//            note.setUserId(userId);
//            if (noteService.addNote(note) > 0) {
//                rs = true;
//            }
//        }
//
//        model.addAttribute("rs", rs);
//        model.addAttribute("notes", noteService.getUserNotes(userId));
//        return "result";
//    }

    @PostMapping("/updateNote")
    public String updateNote(@ModelAttribute("note") Note note, Authentication authentication, Model model) {
        Integer userId = userService.getUserId(authentication.getName());
        boolean rs = false;
        if (userId != null) {
            note.setUserId(userId);
            if (note.getNoteId() == null) {
                rs = noteService.addNote(note) > 0;
            } else {
                rs = noteService.update(note) > 0;
            }
        }
        model.addAttribute("rs", rs);
        model.addAttribute("notes", noteService.getUserNotes(userId));

        return "result";
    }

    @GetMapping("/deleteNote")
    public String deleteNote(@RequestParam("noteId") Integer noteId, Authentication authentication, Model model) {
        String userName = authentication.getName();
        Integer userId = userService.getUserId(userName);

        noteService.delete(noteId);

        model.addAttribute("notes", noteService.getUserNotes(userId));
        model.addAttribute("rs", true);
        return "result";
    }
}
