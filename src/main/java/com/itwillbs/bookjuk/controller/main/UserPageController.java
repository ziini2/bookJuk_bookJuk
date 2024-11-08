package com.itwillbs.bookjuk.controller.main;

import com.itwillbs.bookjuk.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class UserPageController {
	// 관리자가 회원페이지로도 이동을 할 수 있게 만든다.
    // 이유는 ("/") 하게되면 관리자일때 회원페이지가 아닌 관리자 대시보드 화면으로 넘어가기때문!
	
    @GetMapping("/")
    public String userMain(Model model) {
        log.info("userMain");
        log.info("userRole: {}", SecurityUtil.getUserRoles());
        log.info("userName: {}", SecurityUtil.getUserName());
        log.info("userNum: {}", SecurityUtil.getUserNum());
        //회원의 이름을 가져오기!

        if (SecurityUtil.hasRole("ROLE_INACTIVE")){
            return "redirect:/login/phone"; // 권한이 "ROLE_INACTIVE"라면 리다이렉트
        }
        // 관리자가 로그인했을 때는 관리자 대시보드로 리다이렉트
        if (SecurityUtil.hasRole("ROLE_ADMIN")){
            return "redirect:/admin/dashboard"; // 권한이 "ROLE_ADMIN"라면 리다이렉트
        }
        
        model.addAttribute("userName", SecurityUtil.getUserName());
        
     // 일반 사용자일 경우에는 userMain 뷰 반환
        return "userMain";
    }
    
    // 관리자도 회원페이지로 이동할 수 있게 하는 메서드(버튼클릭시)
    @GetMapping("/userMain")
    public String adminToUserMain() {
    	// 관리자가 요청한 경우에만 해당 경로 호출
    	if(SecurityUtil.hasRole("ROLE_ADMIN")) {
    		return "userMain";
    	}
    	 return "redirect:/admin/dashboard";
    	
    }
    
    
    
    
    
    
}
