import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Component, Injectable, OnInit } from '@angular/core';
import { MaxLengthValidator } from '@angular/forms';
import { Router } from '@angular/router';
import { catchError, map } from 'rxjs';
import Swal from 'sweetalert2';
// import { GetDetailsService } from '../service/data/get-details.service';
// import { ProcessDetailsService } from '../service/data/process-details.service';
// import { ProcessDetailsService } from '../service/data/process-details.service';

export class PensionerDetails{
  constructor(
{ name, dateOfBirth, pan, salary, allowance, pensionType, bank }: {
  name: String; dateOfBirth: String; pan: String; salary: String; allowance: String; pensionType: String; bank: [
    bankName: String,
    accountNumber: String,
    bankType: String
  ];
} ){
  
    }
}

@Component({
  selector: 'app-fetchdetails',
  templateUrl: './fetchdetails.component.html',
  styleUrls: ['./fetchdetails.component.css']
})

export class FetchdetailsComponent implements OnInit {

  length:number=0;
  aadhaarNumber:string='';
  errorMessage:string='';
  invalidAadhaar=false;
  penDet:any=[];
  penPost:any=[];
  showGet=true;
  showFetch=false;
  showPost=false;
  showProcess=false;
  loadingGet=true;
  loadingPost=true;
  reLogin=false;
  failGetDetailsOfPension=false;
  failProcessPension=false;

  // pensionerDetails:PensionerDetails=[];
  baseURLPost: string = 'http://localhost:8765/ProcessPension';
  baseURLGet: string = 'http://localhost:8765/pensionerDetailByAadhaar';
  
  constructor(private router: Router,
    private http:HttpClient,) {}

  ngOnInit(): void {
  }

  filterValue(e:KeyboardEvent){
    var charCode = (e.which) ? e.which : e.keyCode;
    // Only Numbers 0-9
    if ((charCode < 48 || charCode > 57)) {
        e.preventDefault();
        return false;
    } else
    {
      return true;
    }
  }

  onKeyUp(e:Event){
    this.length=(e.target as HTMLInputElement).value.length;
  }

  delay(milliseconds : number) {
    return new Promise(resolve => setTimeout( resolve, milliseconds));
  }

  showGetDetails(){
    if(this.length===12){
      return !this.showGet;
    }
    this.showPost=false;
    return this.showGet;
  }

  fetchDetails(){
    this.showPost=true;
    console.log(this.aadhaarNumber);
    return this.http.get<any>(`${this.baseURLGet}/${this.aadhaarNumber}`)
    .subscribe(
      response => {
        console.log(response);
        this.invalidAadhaar=false;
        this.reLogin=false;
        this.failGetDetailsOfPension=false;
        // this.penDet=JSON.parse(JSON.stringify(response));

        for(let key in response){
          if(response.hasOwnProperty(key)){
            this.penDet.push(response[key]);
          }
        }
        this.showFetch=true;
      }, (error) => {                              //Error callback
        console.error('error caught in component')
        if(error.status===404){
        this.errorMessage = "Aadhaar Number Not Found!";
        this.invalidAadhaar=true;
        this.reLogin=false;
        this.failGetDetailsOfPension=false;
        }
        if(error.status===500){
          this.errorMessage="Your Auth Token has expired, Please login again..."
          this.reLogin=true;
          this.invalidAadhaar=false;
          this.failGetDetailsOfPension=false;
        }
        if(error.status===0){
          this.errorMessage="Our service seems to be down... Please try after some time..."
          this.failGetDetailsOfPension=true;
          this.invalidAadhaar=false;
          this.reLogin=false;
        }
        //throw error;   //You can also throw the error to a global error handler
      }
      )
      
  }


    postDetails(){
      return this.http.post<any>(`${this.baseURLPost}`,JSON.stringify({
        name:this.penDet[0],
        dateOfBirth:this.penDet[1],
        pan:this.penDet[2],
        aadhaarNumber:this.aadhaarNumber,
        salary:this.penDet[3],
        allowance:this.penDet[4],
        pensionType:this.penDet[5]}))
        .subscribe(
          response => {console.log(response)
            this.reLogin=false;
            this.failProcessPension=false;
          for(let key in response){
            if(response.hasOwnProperty(key)){
              this.penPost.push(response[key]);
            }
          }
          this.delay(5000);
          this.showProcess=true
        }, (error) => {                              //Error callback
          console.error('error caught in component')
          if(error.status===400){
            this.errorMessage="Your Auth Token has expired, Please login again..."
            this.reLogin=true;
            this.failProcessPension=false;
            Swal.fire({
              title: 'Error',
              html: `<div class="alert alert-warning">${this.errorMessage}</div>`,
              icon: 'error',
              showCancelButton: false,
              confirmButtonText: 'Navigate to Login Page...',
            }).then((result) => {
        
              if (result.isConfirmed) {
                this.router.navigate(['']);
              }
          });
          }
          if(error.status===0){
            this.errorMessage="Our service seems to be down... Please try after some time..."
            this.failProcessPension=true;
            this.reLogin=false;
            Swal.fire({
              title: 'Error',
              html: `<div class="alert alert-warning">${this.errorMessage}</div>`,
              icon: 'error',
              showCancelButton: false,
              confirmButtonText: 'Ok',
            }).then((result) => {
        
              if (result.isConfirmed) {
                this.router.navigate(['getPensionDetails']);
              }
          });
          }
          //throw error;   //You can also throw the error to a global error handler
        }
        );
    }

    handlePost(){
      Swal.fire(
        {
        title: 'Please wait... we are processing details',
        icon:'info',
        confirmButtonText: 'View Details',
        didOpen: () => {
          Swal.showLoading()
          setTimeout(() => { Swal.hideLoading() }, 5000)
        },
        willClose: () => {
      if(this.showProcess===true){
      var swal_html='For:'+this.penPost[0]+'<br> Other Details: <br> DOB: '+this.penPost[1]+' | PAN: '+this.penPost[2]+' | Pension Type: '
                    +this.penPost[3]+' | Pension Amount: '+this.penPost[4]+' | Bank Charges: '+this.penPost[5]
      Swal.fire({
        title: 'Pension Processed',
        html: swal_html,
        icon: 'success',
        showCancelButton: false,
        confirmButtonText: 'Ok',
      }).then((result) => {
  
        if (result.isConfirmed) {
          this.router.navigate(['welcome']);
        }
    })
  }
}
})
}
}
