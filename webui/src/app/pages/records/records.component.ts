import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {MatDialog, MatDialogRef} from "@angular/material";
import {AddRecordDialog} from "../../components/dialog/add-record-dialog.component";
import {RecordsService} from "../../services/api/records.service";

@Component({
    selector: 's-customers-pg',
    templateUrl: './records.component.html',
    styleUrls: ['./records.scss'],
})

export class RecordsComponent implements OnInit {

    columns: any[];
    rows: any[];
    pageSize: number = 10;
    currentPage: number = 0;
    isLastPageLoaded: boolean = false;
    isLoading: boolean = false;

    constructor(private router: Router, private recordService: RecordsService,
                public dialog: MatDialog) {
    }

    ngOnInit() {
        let me = this;
        me.getPageData();

        this.columns = [
            {prop: "id", name: "Id", width: 80},
            {prop: "email", name: "Email", width: 200},
            {prop: "birthdayType", name: "Type", width: 100},
            {prop: "nickname", name: "Nickname", width: 120},
            {prop: "birthdayString", name: "Birthday", width: 200},
            {prop: "nextBirthdayString", name: "NextBirthdayTime", width: 200},
        ];
    }

    addRecord(): void {
        console.log("add Record... " + this.dialog);
        let dialogRef: MatDialogRef<AddRecordDialog> = this.dialog //
            .open(AddRecordDialog, {
                width: '250px',
            });

        dialogRef.afterClosed() //
            .subscribe(result => {
                console.log('The dialog was closed ' + result);
                if (result != undefined && result != null) {
                    this.recordService.addRecord(result) //
                        .subscribe((data) => { //
                            console.log(data);
                        });
                }
            });
    }

    getPageData(isAppend: boolean = false) {

        if (this.isLastPageLoaded === false) {
            let me = this;
            me.isLoading = true;
            this.recordService //
                .getRecords(this.currentPage, this.pageSize) //
                .subscribe((data) => {
                    me.isLastPageLoaded = !data.hasNextPage;
                    me.currentPage = data.currentPage + 1;
                    if (isAppend === true) {
                        me.rows = me.rows.concat(data.items);
                    } else {
                        me.rows = data.items;
                    }
                    me.isLoading = false;
                });
        }
    }

    onScroll() {
        console.log("scroll to bottom, load more data!");
        if (this.isLoading === false) {
            this.getPageData(true);
        }
    }

    onTableRowActive($event: any) {
        if ($event.type !== 'click') {
            // pass
            return;
        }
        console.log($event);
        this.recordService.deleteRecord($event.row.id)
            .subscribe(data => {
                console.log(data);
            });
    }
}
