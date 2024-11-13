package com.itwillbs.bookjuk.controller.rent;

import com.itwillbs.bookjuk.dto.RentResponseDTO;
import com.itwillbs.bookjuk.service.rent.RentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class RentController {

    private final RentService rentService;

    @GetMapping("rent")
    public String rent() {
        return "rent/rent";
    }

    @GetMapping("/rent/search")
    @ResponseBody
    public ResponseEntity<RentResponseDTO> searchRent(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "criteria", required = false) String criteria,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "rented", required = false) Boolean rented,
            @RequestParam(value = "returned", required = false) Boolean returned) {

        log.info("criteria: {}, keyword: {}, rented: {}, returned: {}", criteria, keyword, rented, returned);

        RentResponseDTO responseDTO = (criteria == null || keyword == null) ?
                rentService.findAllWithDTO(rented, returned, page, size) :
                rentService.findAllBySearchWithDTO(criteria, keyword, rented, returned, page, size);

        return ResponseEntity.ok(responseDTO);
    }
}
