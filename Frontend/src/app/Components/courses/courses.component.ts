import { Component, OnInit } from '@angular/core';
import { CourseService, CourseResponse } from '../../Services/course/course.service';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css'
})
export class CoursesComponent implements OnInit {
  courses: CourseResponse[] = [];
  loading = false;
  error = '';

  constructor(private courseService: CourseService) {}

  ngOnInit(): void { this.load(); }

  load() {
    this.loading = true;
    this.courseService.getAllCourses().subscribe({
      next: (data) => { this.courses = data; this.loading = false; },
      error: () => { this.error = 'Failed to load courses'; this.loading = false; }
    });
  }

  enroll(course: CourseResponse) {
    this.courseService.enroll(course.id).subscribe({
      next: () => this.load(),
      error: (err: any) => alert(err.status === 401 ? 'Please login to enroll' : 'Enrollment failed')
    });
  }

  unenroll(course: CourseResponse) {
    this.courseService.unenroll(course.id).subscribe({
      next: () => this.load(),
      error: () => alert('Unenroll failed')
    });
  }
}
