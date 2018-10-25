package com.xhinliang.birthdays.api.controller;

import com.google.common.annotations.VisibleForTesting;
import com.xhinliang.birthdays.api.view.BirthRecordView;
import com.xhinliang.birthdays.common.config.security.JwtTokenHelper;
import com.xhinliang.birthdays.common.config.security.model.JwtTokenUser;
import com.xhinliang.birthdays.common.db.constant.BirthdayType;
import com.xhinliang.birthdays.common.db.model.BirthRecordModel;
import com.xhinliang.birthdays.common.dto.BirthRecordDto;
import com.xhinliang.birthdays.common.dto.NormalResponse;
import com.xhinliang.birthdays.common.dto.PageItem;
import com.xhinliang.birthdays.common.service.impl.BirthRecordServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author xhinliang
 */
@RestController
@RequestMapping(value = "/api/birthRecord", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(tags = {"BirthRecord"})
public class BirthRecordController {

    private final JwtTokenHelper tokenHelper;

    private final BirthRecordServiceImpl birthRecordService;

    @Autowired
    public BirthRecordController(JwtTokenHelper tokenHelper, BirthRecordServiceImpl birthRecordService) {
        this.tokenHelper = tokenHelper;
        this.birthRecordService = birthRecordService;
    }

    @ApiOperation(value = "Add Record", response = NormalResponse.class)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public NormalResponse add(@RequestBody BirthRecordDto request) {
        JwtTokenUser tokenUser = tokenHelper.getCurrentTokenUser();
        BirthRecordModel model = birthRecordService.create(tokenUser.getInternalUser().getId(), request.getEmail(),
            request.getNickname(), BirthdayType.of(request.getBirthdayType()),
            request.getBirthTime());
        request.setId(model.getId());
        return NormalResponse.ofData(request);
    }

    @ApiOperation(value = "Delete Record", response = NormalResponse.class)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public NormalResponse delete(@RequestBody DeleteRequest request) {
        long recordId = request.getRecordId();
        birthRecordService.deleteById(recordId);
        return NormalResponse.ofEmptyData();
    }

    @ApiOperation(value = "List your record", response = NormalResponse.class)
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public NormalResponse list(
        @ApiParam(value = "page") @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
        @ApiParam(value = "between 1 to 1000") @RequestParam(value = "size", defaultValue = "20", required = false) Integer size) {
        JwtTokenUser tokenUser = tokenHelper.getCurrentTokenUser();
        PageItem<BirthRecordModel> records = birthRecordService
            .findBirthRecords(tokenUser.getInternalUser().getId(), size, page);
        return NormalResponse.ofData(records.map(BirthRecordView::new));
    }

    /**
     * delete request
     */
    @Data
    @VisibleForTesting
    static class DeleteRequest {
        private long recordId;
    }
}
