import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface UploadedFile {
  id: number;
  publicId: string;
  fileUrl: string;
  semester?: string;
  year?: string;
  courseName?: string;
  uploadedAt: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}

@Injectable({ providedIn: 'root' })
export class PaperModerationService {
  private baseUrl = 'http://localhost:8080/api/admin/files';

  constructor(private http: HttpClient) {}

  getPending(): Observable<UploadedFile[]> {
    return this.http.get<UploadedFile[]>(`${this.baseUrl}/pending`);
  }

  approve(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/approve`, {});
  }

  reject(id: number): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}/reject`, {});
  }
}