import {Component, OnInit} from '@angular/core';
import {LoginService} from '../../services/api/login.service';
import {Router} from '@angular/router';

@Component({
    selector: 's-login-pg',
    templateUrl: './login.component.html',
    styleUrls: ['./login.scss'],
})

export class LoginComponent implements OnInit {
    model: any = {};
    errMsg: string = '';

    constructor(private router: Router,
                private loginService: LoginService) {
    }

    ngOnInit() {
        // reset login status
        this.loginService.logout(false);
    }

    login() {
        this.loginService.getToken(this.model.username, this.model.password)
            .subscribe(landing => {
                    this.errMsg = 'login success!';
                    this.router.navigate([landing]);
                },
                err => {
                    this.errMsg = err.message;
                }
            );
    }

    goRegister() {
        this.router.navigate(['/register'])
    }

    onSignUp() {
        this.router.navigate(['signup']);
    }
}
