import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface QPaper {
  id: number;
  publicId: string;
  fileUrl: string;
  semester: string | null;
  year: string | null;
  courseName: string | null;
  uploadedAt: string;
}
@Injectable({
  providedIn: 'root'
})
export class PaperDashboardService {

  private readonly API_URL = 'http://localhost:8080/api/files/papers';

  constructor(private http: HttpClient) { }

  getQpapers(): Observable<QPaper[]> {
    return this.http.get<QPaper[]>(this.API_URL);
  }
}
