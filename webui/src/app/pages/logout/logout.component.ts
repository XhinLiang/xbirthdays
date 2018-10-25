import {Component} from '@angular/core';
import {UserInfoService} from '../../services/user-info.service';

@Component({
    selector: 's-logout-pg',
    templateUrl: './logout.component.html',
    styleUrls: ['./logout.scss'],
})

export class LogoutComponent {
    constructor(private userInfoService: UserInfoService) {
        this.userInfoService.removeUserInfo();
    }
}
