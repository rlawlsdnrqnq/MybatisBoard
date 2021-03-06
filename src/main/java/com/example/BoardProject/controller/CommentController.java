package com.example.BoardProject.controller;

import com.example.BoardProject.adapter.GsonLocalDateTimeAdapter;
import com.example.BoardProject.domain.CommentDto;
import com.example.BoardProject.service.CommentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.dao.DataAccessException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @RequestMapping(value = {"/comments", "/comments/{id}"}, method = {RequestMethod.POST, RequestMethod.PATCH})
    public JsonObject registerComment(@PathVariable(value = "id", required = false) Long id, @RequestBody final CommentDto commentDto) {

        JsonObject jsonObj = new JsonObject();

        try {
            boolean isRegistered = commentService.registerComment(commentDto);
            jsonObj.addProperty("result", isRegistered);

        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }

        return jsonObj;
    }

    @GetMapping(value = "/comments/{boardId}")
    public JsonObject getCommentList(@PathVariable("boardId") Long boardId, @ModelAttribute("commentDto") CommentDto commentDto) {

        JsonObject jsonObj = new JsonObject();

        List<CommentDto> commentList = commentService.getCommentList(commentDto);
        if (!CollectionUtils.isEmpty(commentList)) {
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeAdapter()).create();
            JsonArray jsonArr = gson.toJsonTree(commentList).getAsJsonArray();
            jsonObj.add("commentList", jsonArr);
        }
        return jsonObj;
    }

    @DeleteMapping(value = "/comments/{id}")
    public JsonObject deleteComment(@PathVariable("id") final Long id) {
        JsonObject jsonObj = new JsonObject();

        try {
            boolean isDeleted = commentService.deleteComment(id);
            jsonObj.addProperty("result", isDeleted);

        } catch (DataAccessException e) {
            jsonObj.addProperty("message", "데이터베이스 처리 과정에 문제가 발생하였습니다.");

        } catch (Exception e) {
            jsonObj.addProperty("message", "시스템에 문제가 발생하였습니다.");
        }

        return jsonObj;
    }
}

