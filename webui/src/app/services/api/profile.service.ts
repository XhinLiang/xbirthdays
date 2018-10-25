import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';
import {ApiRequestService} from './api-request.service';
import {HttpParams} from "@angular/common/http";

@Injectable()
export class ProfileService {

    constructor(private apiRequest: ApiRequestService) {
    }

    getMyProfile(): Subject<any> {
        let profileSubject = new Subject<any>();
        this.apiRequest.get('api/xuser/profile')
            .subscribe(jsonResp => {
                console.log('profile response:');
                console.log(jsonResp);
                profileSubject.next(jsonResp);
            });

        return profileSubject;
    }

}
