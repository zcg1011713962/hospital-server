package com.hs.service;


import com.hs.db.entity.DoctorSchedule;
import com.hs.entity.LogicResponse;
import com.hs.entity.PageResponse;
import com.hs.entity.bo.DoctorBO;
import com.hs.entity.bo.DoctorScheduleBO;
import com.hs.entity.dto.DoctorDTO;
import com.hs.entity.dto.DoctorScheduleDTO;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface DoctorService {

    CompletableFuture<LogicResponse<PageResponse<DoctorBO>>> searchDoctor(DoctorDTO doctorDTO);

    Mono<LogicResponse<String>> importDoctors(FilePart filePart);

    Mono<LogicResponse<String>> importDoctorSchedule(FilePart filePart);

    CompletableFuture<LogicResponse<PageResponse<DoctorScheduleBO>>> searchDoctorBySchedule(DoctorScheduleDTO doctorScheduleDTO);

}
