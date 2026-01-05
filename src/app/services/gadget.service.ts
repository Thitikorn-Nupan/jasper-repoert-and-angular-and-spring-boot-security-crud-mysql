import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Gadget} from "../entities/gadget";
import {environment} from "../../environments/environment.development";

@Injectable({
  providedIn: 'root'
})
export class GadgetService {

  private readonly baseUrl: string = environment.baseUrl+'/gadget';

  constructor(private readonly httpClient: HttpClient) { }

  public selectAll(token:string): Observable<Gadget[]> {
    const headers = {
      'Authorization': 'Bearer '+token,
    };
    return this.httpClient.get<Gadget[]>(this.baseUrl+'/selectAll',{headers});
  }

  public selectAllByBrand(token:string,brand:string): Observable<Gadget[]> {
    const headers = {
      'Authorization': 'Bearer '+token,
    };
    return this.httpClient.get<Gadget[]>(this.baseUrl+'/selectAllByBrand?value='+brand,{headers});
  }

  public selectColumnAll(token:string): Observable<string[]> {
    const headers = {
      'Authorization': 'Bearer '+token,
    };
    return this.httpClient.get<string[]>(this.baseUrl+'/selectColumnAllByBrand',{headers});
  }

  public report(token:string,type : 'PDF' | 'XLSX'): Observable<any> {
    const headers = {
      'Authorization': 'Bearer '+token,
    };
    const fileType : any = {
      fileExtension: type,
    }
    return this.httpClient.post(this.baseUrl+'/report', fileType ,{
      observe: 'response', // Get the full HttpResponse
      responseType: 'blob', // Specify the response body type as a blob
      headers
    }); // Use 'blob' for binary data
  }

  public reportByBrand(token:string,type : 'PDF' | 'XLSX',brand:string): Observable<any> {
    const headers = {
      'Authorization': 'Bearer '+token,
    };
    const fileType : any = {
      fileExtension: type,
    }
    return this.httpClient.post(this.baseUrl+'/reportByBrand?value='+brand, fileType ,{
      observe: 'response', // Get the full HttpResponse
      responseType: 'blob', // Specify the response body type as a blob
      headers
    }); // Use 'blob' for binary data
  }
}
