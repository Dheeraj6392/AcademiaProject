import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Services/auth/auth.service';

interface Course {
  id: number;
  title: string;
  weeks: number;
  lessons: number;
  students: number;
  rating: number;
  price: number;
  imageUrl: string;
  description: string;
}

@Component({
  selector: 'app-my-courses',
  templateUrl: './my-courses.component.html',
  styleUrls: ['./my-courses.component.css']
})
export class MyCoursesComponent implements OnInit {
  courses: Course[] = [];

  constructor(private auth: AuthService) {}

  ngOnInit(): void {
    this.fetchMyCourses();
  }

  fetchMyCourses() {
    this.auth.getMyCourses().subscribe({
      next: (data) => {
        this.courses = data;
      },
      error: (err) => {
        console.error('Failed to load my courses', err);
      }
    });
  }

  unenroll(course: Course) {
    this.auth.unenroll(course.id).subscribe({
      next: () => {
        alert(`Unenrolled from: ${course.title}`);
        this.fetchMyCourses();
      },
      error: (err) => {
        alert('Unenroll failed.');
        console.error('Unenroll error', err);
      }
    });
  }
}