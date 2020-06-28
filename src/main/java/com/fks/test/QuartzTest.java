package com.fks.test;

import com.fks.controller.QuartzController;
import com.fks.domain.JobDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring*.xml")
public class QuartzTest {

    @Autowired
    private QuartzController quartzController;

    @Test
    public void TestList() throws SchedulerException {
        List<JobDto> jobDtos = quartzController.listJob();
        System.out.println(jobDtos);
    }

    @Test
    public void TestAdd() {
        JobDto jobDto = new JobDto();
        jobDto.setJobName("测试定时任务");
        jobDto.setJobGroupName("default");
        jobDto.setTriggerName("trigger_1");
        jobDto.setTriggerGroupName("triggerGroup_1");
        jobDto.setJobClass("com.fks.taskJob.QuartzJob");
        jobDto.setCronExpression("0/10 * * * * ?");
        Map<String, Object> result = quartzController.save(jobDto);
        System.out.println(result.get("code"));
    }

    @Test
    public void TestPause() {
        Map<String, Object> result = quartzController.stopJob("测试定时任务", "default");
        System.out.println(result.get("code"));
    }

    @Test
    public void TestResume() {
        Map<String, Object> result = quartzController.resumeJob("测试定时任务", "default");
        System.out.println(result.get("code"));
    }
}
