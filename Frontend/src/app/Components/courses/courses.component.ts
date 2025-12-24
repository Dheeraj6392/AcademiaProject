import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Services/auth/auth.service';

interface Course {
  id: number;
  title: string;
  weeks: number;
  lessons: number;
  students: number;
  rating: number; // 0-5
  price: number;
  imageUrl: string; // align with backend entity
  description: string;
}

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.css'
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.fetchCourses();
    this.fetchEnrolledIds();
  }

  fetchCourses() {
    // Show all courses on this page
    this.auth.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data;
      },
      error: (err) => {
        console.error('Failed to load courses', err);
      }
    });
  }

  enrolledIds = new Set<number>();

  private updateEnrolledIds() {
    // Refresh enrolled course IDs from server
    this.fetchEnrolledIds();
  }

  private fetchEnrolledIds() {
    this.auth.getMyCourses().subscribe({
      next: (my) => {
        this.enrolledIds = new Set(my.map(c => c.id));
      },
      error: (err) => {
        console.error('Failed to load enrolled courses', err);
      }
    });
  }

  isEnrolled(courseId: number) {
    return this.enrolledIds.has(courseId);
  }

  enroll(course: Course) {
    this.auth.enroll(course.id).subscribe({
      next: () => {
        alert(`Enrolled in: ${course.title}`);
        this.fetchCourses(); // refresh all courses list
        this.fetchEnrolledIds(); // refresh enrolled state
      },
      error: (err) => {
        if (err.status === 401) {
          alert('Please login to enroll in courses');
        } else {
          alert('Enrollment failed.');
        }
        console.error('Enroll error', err);
      }
    });
  }

  unenroll(course: Course) {
    this.auth.unenroll(course.id).subscribe({
      next: () => {
        alert(`Unenrolled from: ${course.title}`);
        this.fetchCourses(); // refresh all courses list
        this.fetchEnrolledIds(); // refresh enrolled state
      },
      error: (err) => {
        alert('Unenroll failed.');
        console.error('Unenroll error', err);
      }
    });
  }
}
