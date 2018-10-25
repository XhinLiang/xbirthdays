import {Component, ViewEncapsulation} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {LoginService} from './services/api/login.service';
import {UserInfoService} from './services/user-info.service';

import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/operator/switchMap';

@Component({
    selector: 'home-comp',
    templateUrl: './home.component.html',
    styleUrls: ['./home.scss'],
    encapsulation: ViewEncapsulation.None
})
export class HomeComponent {

    public showAppAlert: boolean = false;
    public appHeaderItems = [
        {
            label: 'Dashboard',
            href: '/home/dashboard',
            subNav: [
                {label: "Profile", href: "/home/dashboard/profile"},
                {label: "Stats", href: "/home/dashboard/stat"}
            ]
        },
        {label: 'Records', href: '/home/records', subNav: []},
    ];

    public selectedHeaderItemIndex: number = 0;
    public selectedSubNavItemIndex: number = 1;
    public userName: string = "";

    constructor(private router: Router,
                private activeRoute: ActivatedRoute,
                private loginService: LoginService,
                private userInfoService: UserInfoService) {
        // This block is to retrieve the data from the routes (routes are defined in xhinliang-routing.module.ts)
        router.events
            .filter(event => event instanceof NavigationEnd)
            .map(_ => this.router.routerState.root)
            .map(route => {
                while (route.firstChild) route = route.firstChild;
                ;
                return route;
            })
            .mergeMap(route => route.data)
            .subscribe(data => {
                console.log("Route data===: ", data[0]);
                this.selectedHeaderItemIndex = data[0] ? data[0].selectedHeaderItemIndex : -1;
                this.selectedSubNavItemIndex = data[0] ? data[0].selectedSubNavItemIndex : -1;
            });
        this.userName = this.userInfoService.getUserName();

    }

    navbarSelectionChange(val) {
        // console.log(val);
    }

    closeAppAlert() {
        this.showAppAlert = false;
    }

}
