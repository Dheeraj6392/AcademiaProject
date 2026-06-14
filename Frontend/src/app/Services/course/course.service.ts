import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface CourseResponse {
  id: string;
  title: string;
  description: string;
  weeks: number;
  lessons: number;
  price: number;
  imageUrl: string;
  enrolledCount: number;
  enrolled: boolean;
  createdAt: string;
}

export interface CourseRequest {
  title: string;
  description: string;
  weeks: number;
  lessons: number;
  price: number;
  imageUrl: string;
}

@Injectable({ providedIn: 'root' })
export class CourseService {
  private base = 'http://localhost:8080/api/v1/courses';

  constructor(private http: HttpClient) {}

  getAllCourses(): Observable<CourseResponse[]> {
    return this.http.get<CourseResponse[]>(this.base);
  }

  getMyCourses(): Observable<CourseResponse[]> {
    return this.http.get<CourseResponse[]>(`${this.base}/my`);
  }

  enroll(id: string): Observable<CourseResponse> {
    return this.http.post<CourseResponse>(`${this.base}/${id}/enroll`, {});
  }

  unenroll(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}/enroll`);
  }

  createCourse(req: CourseRequest): Observable<CourseResponse> {
    return this.http.post<CourseResponse>(this.base, req);
  }

  deleteCourse(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
