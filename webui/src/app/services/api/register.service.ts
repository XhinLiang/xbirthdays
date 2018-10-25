import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

import {Observable} from 'rxjs/Observable';
import {BehaviorSubject} from 'rxjs/BehaviorSubject';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {UserInfoService} from '../user-info.service';
import {ApiRequestService} from './api-request.service';
import {logger} from "codelyzer/util/logger";

export interface RegisterRequestParam {
    email: string;
    nickname: string;
    password: string;
}

@Injectable()
export class RegisterService {

    public landingPage = '/login';

    constructor(private router: Router,
                private userInfoService: UserInfoService,
                private apiRequest: ApiRequestService) {
    }


    doRegister(email: string, nickname: string, password: string): Observable<any> {
        const bodyData: RegisterRequestParam = {
            'email': email,
            'nickname': nickname,
            'password': password,
        };

        const registerDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]);

        this.apiRequest //
            .post('api/auth/register', bodyData) //
            .subscribe(jsonResp => {
                    if (jsonResp === undefined || jsonResp === null) {
                        logger.error('jsonResp null' + jsonResp);
                        registerDataSubject.error(new Error("jsonResp null"));
                        return;
                    }
                    if (jsonResp.code != 0) {
                        logger.error('jsonResp err' + jsonResp);
                        registerDataSubject.error(new Error("jsonResp err: " + jsonResp.message));
                        return;
                    }
                    registerDataSubject.next(this.landingPage);
                },
                err => {
                    registerDataSubject.error(err);
                });

        return registerDataSubject;
    }
}
