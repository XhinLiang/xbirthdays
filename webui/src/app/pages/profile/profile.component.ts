import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import 'rxjs/add/operator/mergeMap';
import {ProfileService} from "../../services/api/profile.service";


@Component({
    selector: 's-order_stats-pg',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.scss'],
})

export class ProfileComponent implements OnInit {
    public myProfile = {
        email: 'example@gmail.com',
        nickname: 'example',
        birthdayString: '1970-1-1',
        nextBirthdayString: '1971-1-1',
    };

    constructor(private router: Router, private orderService: ProfileService) {
    }

    ngOnInit() {
        this.getPageData()
    }

    getPageData() {
        let me = this;

        me.orderService.getMyProfile()
            .subscribe(json => {
                me.myProfile = json.data;
            });
    }
}
