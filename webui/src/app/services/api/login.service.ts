import {Injectable} from '@angular/core';
import {Router} from '@angular/router';

import {BehaviorSubject, Observable} from 'rxjs';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import {UserInfoService} from '../user-info.service';
import {ApiRequestService} from './api-request.service';
import {logger} from "codelyzer/util/logger";

export interface LoginRequestParam {
    email: string;
    password: string;
}

@Injectable()
export class LoginService {

    public landingPage: string = "/home/dashboard/";

    constructor(private router: Router,
                private userInfoService: UserInfoService,
                private apiRequest: ApiRequestService) {
    }


    getToken(username: string, password: string): Observable<any> {
        let bodyData: LoginRequestParam = {
            "email": username,
            "password": password,
        };

        /*
         *   Using BehaviorSubject instead of Subject
         *   In Angular services are initialized before the components, if any component is
         *   subscribing, it will only receive events which are executed after subscription.
         *   therefore if you put a syncronize next() in the service, the component wont get it.
         *
         *   A BehaviourSubject will always provide the values wheather the subscription happened after or before event
        */

        let loginDataSubject: BehaviorSubject<any> = new BehaviorSubject<any>([]); // Will use this BehaviorSubject to emit data that we want after ajax login attempt

        this.apiRequest.post('api/auth/login', bodyData)
            .subscribe(jsonResp => {
                    if (jsonResp === undefined || jsonResp === null) {
                        logger.error('jsonResp null' + jsonResp);
                        loginDataSubject.error(new Error("jsonResp null"));
                        return;
                    }
                    if (jsonResp.code != 0) {
                        logger.error('jsonResp err' + jsonResp);
                        loginDataSubject.error(new Error("jsonResp err: " + jsonResp.message));
                        return;
                    }
                    loginDataSubject.next(this.landingPage);
                    // store username and model token in session storage to keep user logged in between page refreshes
                    this.userInfoService.storeUserInfo(JSON.stringify(jsonResp.data));
                },
                err => {
                    loginDataSubject.error(err);
                });

        return loginDataSubject;
    }

    logout(navigatetoLogout = true): void {
        // clear token remove user from local storage to log user out
        this.userInfoService.removeUserInfo();
        if (navigatetoLogout) {
            this.router.navigate(["logout"]);
        }
    }
}
