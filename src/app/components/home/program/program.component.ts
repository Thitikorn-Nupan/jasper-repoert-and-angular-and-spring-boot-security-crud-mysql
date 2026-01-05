import {AfterViewInit, Component, Input, OnInit} from '@angular/core';
import {LoginResponse} from "../../../entities/login-response";
import {GadgetService} from "../../../services/gadget.service";
import {Gadget} from "../../../entities/gadget";
import {HttpResponse} from "@angular/common/http";
import {saveAs} from 'file-saver';
import {Subscription} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'program',
  templateUrl: './program.component.html',
  styleUrl: './program.component.css'
})
export class ProgramComponent implements OnInit  {
  @Input()
  public loginResponse!: LoginResponse;
  protected gadgets! : Gadget[]
  protected brands! : string[]
  protected brand : string = 'CANCEL'
  protected total! : {amount : number , price : number}

  constructor(private readonly gadgetService: GadgetService,private readonly router: Router) {}

  ngOnInit(): void {
    if (this.loginResponse) { // this.loginResponse.token
      this.gadgetService.selectAll(this.loginResponse.token).subscribe((res: Gadget[]) => {
        this.gadgets = res
        this.calculateTotal()
      });
      this.gadgetService.selectColumnAll(this.loginResponse.token).subscribe((res: string[]) => {
        this.brands = res
        this.brands.push('CANCEL')
      });
    }
  }

  protected onSelectBrand(brand: string) : void{
    this.brand = brand
    if (this.brand === 'CANCEL') {
      this.gadgetService.selectAll(this.loginResponse.token).subscribe((res: Gadget[]) => {
        this.gadgets = res
        this.calculateTotal()
      });
    } else {
      this.gadgetService.selectAllByBrand(this.loginResponse.token,this.brand).subscribe((res: Gadget[]) => {
        this.gadgets = res
        this.calculateTotal()
      });
    }
  }

  private calculateTotal() : void{
    this.total = {
      amount : 0,
      price : 0
    }
    this.gadgets.forEach((gadget: Gadget) => {
      this.total.amount  +=   gadget.amount;
      this.total.price  += (gadget.price * gadget.amount);
    })

  }

  protected onDownloadFile(type: 'PDF'|'XLSX') : Subscription{
    if (this.brand === 'CANCEL') {
      return this.gadgetService.report(this.loginResponse.token,type).subscribe((res: HttpResponse<any>): void => {
        if (res.body && res.headers.has('File-Name')) {
          const filename: string = res.headers.get('File-Name')!
          saveAs(res.body, filename);
        }
      })
    } else {
      return this.gadgetService.reportByBrand(this.loginResponse.token,type,this.brand).subscribe((res: HttpResponse<any>): void => {
        if (res.body && res.headers.has('File-Name')) {
          const filename: string = res.headers.get('File-Name')!
          saveAs(res.body, filename);
        }
      })
    }
  }

  protected onLogout() : void {
    sessionStorage.removeItem('token')
    this.router.navigate(['program-a']).then(() => (this.router.navigate([''])))
  }
}
