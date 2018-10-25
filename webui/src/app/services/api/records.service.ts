import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {ApiRequestService} from './api-request.service';
import {HttpParams} from "@angular/common/http";
import {logger} from "codelyzer/util/logger";
import {BehaviorSubject} from "rxjs/BehaviorSubject";

export interface AddRecordRequestParam {
    email: string;
    nickname: string;
    birthdayType: number;
    birthTime: number;
}

export interface DeleteRecordRequestParam {
    recordId: number;
}

@Injectable()
export class RecordsService {

    constructor(private apiRequest: ApiRequestService) {
    }

    getRecords(page?: number, size?: number): Observable<any> {
        let params: HttpParams = new HttpParams();
        params = params.append('page', typeof page === "number" ? page.toString() : "1");
        params = params.append('size', typeof size === "number" ? size.toString() : "10");

        let customerListSubject = new Subject<any>(); // Will use this subject to emit data that we want

        this.apiRequest.get('api/birthRecord/list', params)
            .subscribe(jsonResp => {
                customerListSubject.next(jsonResp.data);
            });

        return customerListSubject;
    }


    addRecord(bodyData: AddRecordRequestParam): Observable<any> {
        const addRecordDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);
        this.apiRequest //
            .post('api/birthRecord/add', bodyData) //
            .subscribe(jsonResp => {
                    if (jsonResp === undefined || jsonResp === null) {
                        logger.error('jsonResp null' + jsonResp);
                        addRecordDataSubject.error(new Error("jsonResp null"));
                        return;
                    }
                    if (jsonResp.code != 0) {
                        logger.error('jsonResp err' + jsonResp);
                        addRecordDataSubject.error(new Error("jsonResp err: " + jsonResp.message));
                        return;
                    }
                    addRecordDataSubject.next(jsonResp);
                },
                err => {
                    addRecordDataSubject.error(err);
                });

        return addRecordDataSubject;
    }

    deleteRecord(id: number): Observable<any> {
        let bodyData: DeleteRecordRequestParam = {
            recordId: id,
        };
        const deleteRecordDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);
        this.apiRequest //
            .post('api/birthRecord/delete', bodyData) //
            .subscribe(jsonResp => {
                    if (jsonResp === undefined || jsonResp === null) {
                        logger.error('jsonResp null' + jsonResp);
                        deleteRecordDataSubject.error(new Error("jsonResp null"));
                        return;
                    }
                    if (jsonResp.code != 0) {
                        logger.error('jsonResp err' + jsonResp);
                        deleteRecordDataSubject.error(new Error("jsonResp err: " + jsonResp.message));
                        return;
                    }
                    deleteRecordDataSubject.next(jsonResp);
                },
                err => {
                    deleteRecordDataSubject.error(err);
                });

        return deleteRecordDataSubject;
    }
}
