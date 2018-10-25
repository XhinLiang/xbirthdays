import {Component, Inject} from "@angular/core";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material";
import {AddRecordRequestParam} from "../../services/api/records.service";

@Component({
    selector: 'add-record-dialog',
    templateUrl: 'add-record-dialog.html',
})
export class AddRecordDialog {

    model: any = {
        nickname: null,
        email: null,
        birthdayType: null,
        birthday: null,
    };

    constructor(
        public dialogRef: MatDialogRef<AddRecordDialog>,
        @Inject(MAT_DIALOG_DATA) public data: any) {
    }

    onNoClick(): void {
        this.dialogRef.close();
    }

    onConfirmClick(): void {
        console.log("on confirm click");
        console.log(this.model);
        let obj: AddRecordRequestParam = {
            nickname: this.model.nickname,
            email: this.model.email,
            birthdayType: this.model.birthdayType,
            birthTime: this.model.birthday.getTime()
        };
        this.dialogRef.close(obj);
    }
}
