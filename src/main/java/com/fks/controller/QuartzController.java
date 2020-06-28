package com.fks.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fks.domain.JobDto;
import com.fks.service.IQuartzService;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


/**
 * 定时任务 Controller
 *
 *
 * @author：WangYuanJun
 * @date：2018年1月7日 下午10:15:33
 */

@RestController
@RequestMapping(value = "/quartz")
public class QuartzController {
    @Autowired
    private Scheduler quartzScheduler;

    @Autowired
    private IQuartzService quartzService;

    /**
     * 定时列表页
     *
     * @return
     * @throws SchedulerException
     */
    @RequestMapping(value = "/list")
    public List<JobDto> listJob() throws SchedulerException {
        List<JobDto> jobInfos = this.getSchedulerJobInfo();
        return jobInfos;
    }

    /**
     * 新建job
     *
     * @param jobDto
     * @return
     */
    @RequestMapping(value = "/add")
    public Map<String, Object> save(JobDto jobDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            quartzService.addJob(jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), Class.forName(jobDto.getJobClass()), jobDto.getCronExpression());
            result.put("code", 0);
            result.put("msg", "增加操作成功");
        } catch (Exception e) {
            result.put("code", -1);
            result.put("msg", "增加操作失败");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 编辑job
     *
     * @param jobDto
     * @return
     */
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Map<String, Object> edit(JobDto jobDto) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean temp = quartzService.modifyJobTime(jobDto.getOldJobName(), jobDto.getOldJobGroupName(), jobDto.getOldTriggerName(), jobDto.getOldTriggerGroupName(), jobDto.getJobName(), jobDto.getJobGroupName(), jobDto.getTriggerName(), jobDto.getTriggerGroupName(), jobDto.getCronExpression());
            if (temp) {
                result.put("code", 0);
                result.put("msg", "修改操作成功");
            } else {
                result.put("code", -1);
                result.put("msg", "修改操作失败");
            }
        } catch (Exception e) {
            result.put("code", -1);
            result.put("msg", "修改操作失败");
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 暂停job
     *
     * @param jobName
     * @param jobGroupName
     * @return
     */
    @RequestMapping(value = "/stopJob", method = RequestMethod.POST)
    public Map<String, Object> stopJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            result.put("code", -1);
            result.put("msg", "暂停操作失败");
        } else {
            try {
                quartzService.pauseJob(jobName, jobGroupName);
                result.put("code", 0);
                result.put("msg", "暂停操作成功");
            } catch (Exception e) {
                result.put("code", -1);
                result.put("msg", "暂停操作失败");
                e.printStackTrace();
            }

        }
        return result;
    }

    /**
     * 恢复job
     *
     * @param jobName
     * @param jobGroupName
     * @return
     */
    @RequestMapping(value = "/resumeJob", method = RequestMethod.POST)
    public Map<String, Object> resumeJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName) {
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName)) {
            result.put("code", -1);
            result.put("msg", "恢复操作失败");
        } else {
            try {
                quartzService.resumeJob(jobName, jobGroupName);
                result.put("code", 0);
                result.put("msg", "恢复操作成功");
            } catch (Exception e) {
                result.put("code", -1);
                result.put("msg", "恢复操作失败");
                e.printStackTrace();
            }

        }

        return result;
    }

    /**
     * 删除job
     *
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     * @return
     */
    @RequestMapping(value = "/deleteJob", method = RequestMethod.POST)
    public Map<String, Object> deleteJob(@RequestParam("jobName") String jobName, @RequestParam("jobGroupName") String jobGroupName, @RequestParam("triggerName") String triggerName, @RequestParam("triggerGroupName") String triggerGroupName) {
        Map<String, Object> result = new HashMap<>();

        if (StringUtils.isEmpty(jobName) || StringUtils.isEmpty(jobGroupName) || StringUtils.isEmpty(triggerName) || StringUtils.isEmpty(triggerGroupName)) {
            result.put("code", -1);
            result.put("msg", "删除操作失败");
        } else {
            try {
                quartzService.removeJob(jobName, jobGroupName, triggerName, triggerGroupName);
                result.put("code", 0);
                result.put("msg", "删除操作成功");
            } catch (Exception e) {
                result.put("code", -1);
                result.put("msg", "删除操作失败");
            }
        }

        return result;
    }

    private List<JobDto> getSchedulerJobInfo() throws SchedulerException {
        List<JobDto> jobInfos = new ArrayList<>();
        List<String> triggerGroupNames = quartzScheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
            Set<TriggerKey> triggerKeySet = quartzScheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(triggerGroupName));
            for (TriggerKey triggerKey : triggerKeySet) {
                Trigger t = quartzScheduler.getTrigger(triggerKey);
                if (t instanceof CronTrigger) {
                    CronTrigger trigger = (CronTrigger) t;
                    JobKey jobKey = trigger.getJobKey();
                    JobDetail jd = quartzScheduler.getJobDetail(jobKey);
                    JobDto jobInfo = new JobDto();
                    jobInfo.setJobName(jobKey.getName());
                    jobInfo.setJobGroupName(jobKey.getGroup());
                    jobInfo.setTriggerName(triggerKey.getName());
                    jobInfo.setTriggerGroupName(triggerKey.getGroup());
                    jobInfo.setCronExpression(trigger.getCronExpression());
                    jobInfo.setNextFireTime(trigger.getNextFireTime());
                    jobInfo.setPreviousFireTime(trigger.getPreviousFireTime());
                    jobInfo.setStartTime(trigger.getStartTime());
                    jobInfo.setEndTime(trigger.getEndTime());
                    jobInfo.setJobClass(jd.getJobClass().getCanonicalName());
                    // jobInfo.setDuration(Long.parseLong(jd.getDescription()));
                    Trigger.TriggerState triggerState = quartzScheduler.getTriggerState(trigger.getKey());
                    jobInfo.setJobStatus(triggerState.toString());// NONE无,
                    // NORMAL正常,
                    // PAUSED暂停,
                    // COMPLETE完全,
                    // ERROR错误,
                    // BLOCKED阻塞
                    JobDataMap map = quartzScheduler.getJobDetail(jobKey).getJobDataMap();
                    if (null != map && map.size() != 0) {
                        jobInfo.setCount(Long.valueOf((String) map.get("count")));
                        jobInfo.setJobDataMap(map);
                    } else {
                        jobInfo.setJobDataMap(new JobDataMap());
                    }
                    jobInfos.add(jobInfo);
                }
            }
        }
        return jobInfos;
    }
}
