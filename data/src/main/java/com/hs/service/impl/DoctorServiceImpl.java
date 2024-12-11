package com.hs.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.db.entity.*;
import com.hs.db.mapper.DoctorMapper;
import com.hs.db.mapper.UserMapper;
import com.hs.entity.LogicResponse;
import com.hs.entity.PageResponse;
import com.hs.entity.bo.DoctorBO;
import com.hs.entity.bo.DoctorScheduleBO;
import com.hs.entity.dto.DoctorDTO;
import com.hs.entity.dto.DoctorScheduleDTO;
import com.hs.enums.ErrorCode;
import com.hs.listen.DoctorDataListener;
import com.hs.listen.DoctorScheduleListener;
import com.hs.service.DoctorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public CompletableFuture<LogicResponse<PageResponse<DoctorBO>>> searchDoctor(DoctorDTO doctorDTO) {
        return CompletableFuture.supplyAsync(()->{
            QueryWrapper<Doctor> queryWrapper = new QueryWrapper<>();

            if (StringUtils.isNotBlank(doctorDTO.getDepartment())) {
                queryWrapper.eq("department", doctorDTO.getDepartment());
            }
            if (StringUtils.isNotBlank(doctorDTO.getPosition())) {
                queryWrapper.eq("position", doctorDTO.getPosition());
            }

            Page<Doctor> page = new Page<>(doctorDTO.getPageNum(), doctorDTO.getPageSize());
            IPage<Doctor> doctorPage = doctorMapper.selectPage(page, queryWrapper);

            // 将 User 实体转换为 UserBO
            List<DoctorBO> doctorBOList = doctorPage.getRecords().stream()
                    .map(d -> {
                        return DoctorBO.builder()
                                .userId(d.getUserId())
                                .doctorId(d.getDoctorId())
                                .availableSlots(d.getAvailableSlots())
                                .biography(d.getBiography())
                                .position(d.getPosition())
                                .department(d.getDepartment())
                                .specialization(d.getSpecialization()).build();
                    }).collect(Collectors.toList());

            PageResponse<DoctorBO> pageResponse = new PageResponse<>(
                    doctorBOList,
                    doctorPage.getTotal(),
                    doctorPage.getSize(),
                    doctorPage.getCurrent(),
                    doctorPage.getPages()
            );
            return LogicResponse.<PageResponse<DoctorBO>>builder().status(ErrorCode.SUCCESS).data(pageResponse).build();
        });
    }

    @Override
    public Mono<LogicResponse<String>> importDoctors(FilePart filePart) {
        return DataBufferUtils.join(filePart.content()) // Join the Flux<DataBuffer> into a single DataBuffer
                .map(dataBuffer -> {
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    EasyExcel.read(inputStream, DoctorExcel.class, new DoctorDataListener(doctorMapper, userMapper)).sheet().doRead();
                    return LogicResponse.<String>builder().status(ErrorCode.SUCCESS).build();
                });
    }

    @Override
    public Mono<LogicResponse<String>> importDoctorSchedule(FilePart filePart) {
        return DataBufferUtils.join(filePart.content()) // Join the Flux<DataBuffer> into a single DataBuffer
                .map(dataBuffer -> {
                    // Convert DataBuffer to InputStream
                    byte[] bytes = new byte[dataBuffer.readableByteCount()];
                    dataBuffer.read(bytes);
                    InputStream inputStream = new ByteArrayInputStream(bytes);
                    // Process the Excel file using EasyExcel
                    EasyExcel.read(inputStream, ScheduleExcel.class, new DoctorScheduleListener(doctorMapper, userMapper)).sheet().doRead();
                    return LogicResponse.<String>builder().status(ErrorCode.SUCCESS).build();
                });
    }

    @Override
    public CompletableFuture<LogicResponse<PageResponse<DoctorScheduleBO>>> searchDoctorBySchedule(DoctorScheduleDTO doctorScheduleDTO) {

        return CompletableFuture.supplyAsync(()->{
            Page<DoctorSchedule> page = new Page<>(doctorScheduleDTO.getPageNum(), doctorScheduleDTO.getPageSize());
            IPage<DoctorSchedule> doctorSchedulePage = doctorMapper.selectDoctorBySchedule(page ,doctorScheduleDTO);

            // 将 User 实体转换为 UserBO
            List<DoctorScheduleBO> doctorScheduleBOList = doctorSchedulePage.getRecords().stream()
                    .map(d -> {
                        return DoctorScheduleBO.builder()
                                .doctorID(d.getDoctorID())
                                .doctorName(d.getDoctorName())
                                .department(d.getDepartment())
                                .position(d.getPosition())
                                .startTime(d.getStartTime())
                                .endTime(d.getEndTime())
                                .maxAppointments(d.getMaxAppointments())
                                .bookedAppointments(d.getBookedAppointments())
                                .build();
                    }).collect(Collectors.toList());

            PageResponse<DoctorScheduleBO> pageResponse = new PageResponse<>(
                    doctorScheduleBOList,
                    doctorSchedulePage.getTotal(),
                    doctorSchedulePage.getSize(),
                    doctorSchedulePage.getCurrent(),
                    doctorSchedulePage.getPages()
            );
            return LogicResponse.<PageResponse<DoctorScheduleBO>>builder().status(ErrorCode.SUCCESS).data(pageResponse).build();
        });
    }

}
