import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";
import {filter} from "rxjs";

@Component({
  selector: 'menubar',
  templateUrl: './menubar.component.html',
  styleUrl: './menubar.component.css'
})
export class MenubarComponent implements OnInit{
  protected readonly routerLinks: {id:number,route:string,label:string } [] = [
    {id:0,route:'/',label:'Home'},
    {id:1,route:'/program-a',label:'Program A'},
    {id:2,route:'/program-b',label:'Program B'},
    {id:3,route:'/program-c',label:'Program C'},
  ]
  protected id! : number

  constructor(private readonly router: Router ) {}

  ngOnInit(): void {
    // way to get current route
    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe( (navEnd:any) => {
      this.routerLinks.map((routerLink) => {
        if (routerLink.route === navEnd.urlAfterRedirects) {
          this.id = routerLink.id
        }
      })
    });
  }

  protected onClick(id : number) : void {
    this.id = id
  }
}
