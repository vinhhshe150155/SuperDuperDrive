package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getUserNotes(Integer userId);

    @Select("SELECT FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Integer noteId);

    @Insert("INSERT INTO NOTES(noteTitle, noteDescription, userId) VALUES (#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    Integer addNote(Note note);

    @Update("UPDATE NOTES SET noteTitle = #{noteTitle}, noteDescription = #{noteDescription} WHERE noteId = #{noteId}")
    Integer update(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    Integer delete(Integer noteId);
}