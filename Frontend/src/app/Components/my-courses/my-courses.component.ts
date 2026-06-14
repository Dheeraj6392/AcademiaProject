import { Component, OnInit } from '@angular/core';
import { CourseService, CourseResponse } from '../../Services/course/course.service';

@Component({
  selector: 'app-my-courses',
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.css']
})
export class MyCoursesComponent implements OnInit {
  courses: CourseResponse[] = [];
  loading = false;
  error = '';

  constructor(private courseService: CourseService) {}

  ngOnInit(): void { this.load(); }

  load() {
    this.loading = true;
    this.courseService.getMyCourses().subscribe({
      next: (data) => { this.courses = data; this.loading = false; },
      error: () => { this.error = 'Failed to load your courses'; this.loading = false; }
    });
  }

  unenroll(course: CourseResponse) {
    this.courseService.unenroll(course.id).subscribe({
      next: () => this.load(),
      error: () => alert('Unenroll failed')
    });
  }
}
