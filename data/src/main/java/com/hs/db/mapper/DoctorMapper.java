package com.hs.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hs.db.entity.*;
import com.hs.entity.dto.DoctorScheduleDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    void insertBatchDoctors(@Param("doctors") List<DoctorExcel> doctors);

    void insertBatchSchedule(@Param("schedules") List<Schedule> schedules);

    IPage<DoctorSchedule> selectDoctorBySchedule(Page<?> page, @Param("param") DoctorScheduleDTO doctorScheduleDTO);
}
