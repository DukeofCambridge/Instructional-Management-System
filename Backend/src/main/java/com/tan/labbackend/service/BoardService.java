package com.tan.labbackend.service;

import com.tan.labbackend.entity.Board;
import com.tan.labbackend.entity.Course;
import com.tan.labbackend.dao.BoardDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BoardService {
    @Autowired
    BoardDAO boardDAO;
    @Autowired
    CourseService courseService;
    public List<Board> findByCourseId(Integer cid){
        Course course = courseService.get(cid);
        return boardDAO.findAllByCourse(course);
    }
    @Transactional
    public void publish(Board board){
        Board nb = new Board();
        nb.setInfo(board.getInfo());
        nb.setPublisher(board.getPublisher());
        nb.setTitle(board.getTitle());
        nb.setCourse(courseService.get(board.getCourse().getId()));
        Date date =new Date();
        nb.setDate(date);
        boardDAO.save(nb);
    }
    @Transactional
    public void deleteByBoardId(Integer bid){
        boardDAO.deleteById(bid);
    }

}
