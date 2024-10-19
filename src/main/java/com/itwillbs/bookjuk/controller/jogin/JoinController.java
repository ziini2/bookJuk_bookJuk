package com.itwillbs.bookjuk.controller.jogin;


import com.itwillbs.bookjuk.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class JoinController {

    @PostMapping("/join")
    @ResponseBody
    public Map<String, String> join(@RequestBody UserDTO userDTO) {

        return Map.of("RESULT", "SUCCESS");
    }



}
