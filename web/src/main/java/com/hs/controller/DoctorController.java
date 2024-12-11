package com.hs.controller;

import com.hs.entity.BaseResponse;
import com.hs.entity.LogicResponse;
import com.hs.entity.PageResponse;
import com.hs.entity.bo.DoctorBO;
import com.hs.entity.bo.DoctorScheduleBO;
import com.hs.entity.dto.DoctorDTO;
import com.hs.entity.dto.DoctorScheduleDTO;
import com.hs.entity.vo.DoctorScheduleVO;
import com.hs.entity.vo.DoctorVO;
import com.hs.enums.ErrorCode;
import com.hs.service.DoctorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctor/search")
    public Mono<BaseResponse<PageResponse<DoctorVO>>> searchDoctor(DoctorDTO doctorDTO) {
        log.debug("查询医生:{}", doctorDTO);
        CompletableFuture<BaseResponse<PageResponse<DoctorVO>>> future = doctorService.searchDoctor(doctorDTO).thenApply((logicResponse) -> {
            if (logicResponse.getStatus() == ErrorCode.SUCCESS) {
                PageResponse<DoctorBO> pageBO = logicResponse.getData();

                List<DoctorVO> doctorVOList = pageBO.getRecords().stream()
                        .map(d -> {
                            return DoctorVO.builder()
                                    .userId(d.getUserId())
                                    .doctorId(d.getDoctorId())
                                    .availableSlots(d.getAvailableSlots())
                                    .biography(d.getBiography())
                                    .position(d.getPosition())
                                    .department(d.getDepartment())
                                    .specialization(d.getSpecialization()).build();
                        }).collect(Collectors.toList());

                PageResponse<DoctorVO> pageResponse = new PageResponse<>(
                        doctorVOList,
                        pageBO.getTotal(),
                        pageBO.getSize(),
                        pageBO.getCurrent(),
                        pageBO.getPages()
                );
                return BaseResponse.<PageResponse<DoctorVO>>builder()
                        .status(ErrorCode.SUCCESS.getCode())
                        .data(pageResponse)
                        .build();
            }
            return BaseResponse.<PageResponse<DoctorVO>>builder().status(logicResponse.getStatus().getCode()).message(logicResponse.getMsg()).build();
        }).exceptionally((e)->{
            log.error("新增用户异常", e);
            return BaseResponse.<PageResponse<DoctorVO>>builder().status(ErrorCode.FAILED.getCode()).message(ErrorCode.FAILED.getDesc()).build();
        });
        return Mono.fromFuture(future);
    }


    @PostMapping(value = "/doctor/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<LogicResponse<String>> importDoctor(@RequestPart("file") Mono<FilePart> file) {
        log.debug("开始导入医生基础信息文件");
        return file.flatMap(f -> {
            return doctorService.importDoctors(f);
        });
    }


    @PostMapping(value = "/doctor/schedule/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<LogicResponse<String>> importSchedule(@RequestPart("file") Mono<FilePart> file) {
        log.debug("开始导入医生排班计划文件");
        return file.flatMap(f -> {
            return doctorService.importDoctorSchedule(f);
        });
    }


    @PostMapping("/doctor/schedule/import")
    public Mono<BaseResponse<PageResponse<DoctorScheduleVO>>> selectDoctorBySchedule(@RequestBody DoctorScheduleDTO doctorScheduleDTO){
        log.debug("查询医生排班信息");
        CompletableFuture<BaseResponse<PageResponse<DoctorScheduleVO>>> future = doctorService.searchDoctorBySchedule(doctorScheduleDTO).thenApply((logicResponse) -> {
            PageResponse<DoctorScheduleBO> pageBO = logicResponse.getData();

            List<DoctorScheduleVO> doctorScheduleVOList = pageBO.getRecords().stream()
                    .map(d -> {
                        // 是否还有预约号
                        boolean isExistNum = Integer.parseInt(d.getMaxAppointments()) > Integer.parseInt(d.getBookedAppointments());
                        return DoctorScheduleVO.builder()
                                .doctorID(d.getDoctorID())
                                .doctorName(d.getDoctorName())
                                .department(d.getDepartment())
                                .position(d.getPosition())
                                .startTime(d.getStartTime())
                                .endTime(d.getEndTime())
                                .maxAppointments(d.getMaxAppointments())
                                .bookedAppointments(d.getBookedAppointments())
                                .isExistNum(isExistNum).build();
                    }).collect(Collectors.toList());

            PageResponse<DoctorScheduleVO> pageResponse = new PageResponse<>(
                    doctorScheduleVOList,
                    pageBO.getTotal(),
                    pageBO.getSize(),
                    pageBO.getCurrent(),
                    pageBO.getPages()
            );
            return BaseResponse.<PageResponse<DoctorScheduleVO>>builder()
                    .status(ErrorCode.SUCCESS.getCode())
                    .data(pageResponse)
                    .build();
        });
        return Mono.fromFuture(future);
    }


}
