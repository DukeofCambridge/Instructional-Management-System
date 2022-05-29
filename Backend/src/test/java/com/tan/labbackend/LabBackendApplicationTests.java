package com.tan.labbackend;

import com.tan.labbackend.dto.Score;
import com.tan.labbackend.entity.Project;
import com.tan.labbackend.entity.Report;
import com.tan.labbackend.entity.UserProject;
import com.tan.labbackend.service.ProjectService;
import com.tan.labbackend.service.ReportService;
import com.tan.labbackend.service.ScoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LabBackendApplicationTests {
//    @Autowired
//    ScoreService scoreService;
//    @Test
//    void soceServiceTest(){
//        UserProject userProject= new UserProject();
//        userProject.setStudentId(1);
//        userProject.setProjectId(12);
//        userProject.setProjectGrade(33.0);
//        userProject.setSubmitTime(Instant.now());
//        scoreService.mark(userProject);
//    }
//    @Autowired
//    ProjectService projectService;
//    @Test
//    void projectServiceTest(){
//        Project poject = projectService.get(12);
//        System.out.println(poject.toString());
//    }
//    @Autowired
//    ReportService reportService;
//
//    @Test
//    void reportServiceTest(){
//        Report report= new Report();
//        report.setId(555);
//        reportService.deleteByReportId(555);
//    }
//	@Autowired
//	UserService userService;
//	@Autowired
//	RoleService roleService;
//	@Autowired
//	RolePermissionDAO rolePermissionDAO;
//	@Autowired
//	RolePermissionService rolePermissionService;
//	@Autowired
//	PermissionService permissionService;
//	@Autowired
//	UserDAO userDAO;
//	@Autowired
//	ProjectDAO projectDAO;
//	@Autowired
//	CourseDAO courseDAO;
//	@Test
//	void projectGradeDAOTest() {
//		ProjectGrade projectGrade =projectGradeDAO.findByCourseIdAndStudentIdAndProjectId(2,3,1);
//		System.out.println(projectGrade.toString());
//
//		List<ProjectGrade> a=projectGradeDAO.findAllByCourseIdAndStudentId(2,3);
//		for (ProjectGrade grade : a) {
//			System.out.println(grade.toString());
//		}
//	}
//	@Test
//	void projectDAOTest() {
//		Project project = projectDAO.findByCourseIdAndProjectId(2, 1);
//		System.out.println(project.toString());
//
//		List<Project> a = projectDAO.findAllByCourseId(2);
//		for (Project grade : a) {
//			System.out.println(grade.toString());
//		}
//	}
//	@Test
//	void courseDAOTest() {
//
//		List<Course> a = courseDAO.findAll();
//		for (Course grade : a) {
//			System.out.println(grade.toString());
//		}
//	}
//	@Test
//	void userServiceTest() {
//		User user = userService.findByUsername("2323");
//		System.out.println(user.toString());
////		User user=userService.findByUsername("tan");
////		System.out.println(user.toString());
////		List<UserRole> userRoles=user.getUserRoles();
////		userRoles.forEach(userRole -> {
////			userRole.setCourseID(3);
////				});
////		user.setUserRoles(userRoles);
////		userService.updateUserMsg(user);
//	}
//	@Test
//	void roleServiceTest() {
//		List<Role> roles=roleService.listRolesByUser("2323");
//		roles.forEach(role -> {
//			System.out.println(role.toString());
//		});
//	}
//
//	@Test
//	void userRoleServiceTest(){
//		User user=userDAO.findByUsername("2323");
//		System.out.println(user.getId());
//		List<UserRole> a = userRoleService.findAllByUserID(user.getId());
//		//user.setUserRoles(userRole);
//		//List<UserRole> a= userRoleDAO.findAllByUserID(2323);//userRoleService.findAllByUserID(2323);
//		a.forEach(userRole -> {
//			System.out.println(userRole.toString());
//		});
//	}
//	@Test
//	void courseServiceTest(){
////		List<Course> a;// = courseService.findAllbyUsername("2323");
////		Course b = courseService.get(3);
////		System.out.println(b.toString());
////		Course b = new Course();
////		b.setCourseName("testadd");
////		b.setCourseDescription("testadd");
////		courseService.addOrUpdate(b);
////		a.forEach(role ->{
////			System.out.println(a.toString());
////		});
//
//	}
//	@Test
//	void boardServiceTest(){
////		List<Board> a= boardService.findByCourseId(2);
////		Board b = new Board();
////		b.setCourse(courseService.get(3));
////		b.setInfo("testAdd");
////		boardService.addOrUpdate(b);
////		b.setId(2);
////		b.setInfo("testUpdate");
////		boardService.addOrUpdate(b);
////		boardService.deleteByBoardId(3);
//
//		List<UserRoleService.UsernameRolename> a=userRoleService.findAllByCourseId(2);
//		a.forEach(role->{
//			System.out.println(role.toString());
//		});
//	}
}
