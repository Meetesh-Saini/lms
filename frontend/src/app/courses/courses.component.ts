import { Component, inject, OnInit } from '@angular/core';
import { MatDialog, MatDialogTitle } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { Router, RouterModule } from '@angular/router';
import { CourseModalComponent } from '../course-modal/course-modal.component';
import { NavbarComponent } from '../navbar/navbar.component';
import { NgFor, NgIf } from '@angular/common';
import { NotificationService } from '../notification.service';
import { EnrollmentService } from '../enrollment.service';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [MatCardModule, MatButtonModule, RouterModule, NavbarComponent, NgFor, NgIf, MatDialogTitle],
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.scss'],
})
export class CoursesComponent implements OnInit {
  courses: any[] = [];
  enrolledCourses: any[] = [];

  private api = inject(ApiService);
  private dialog = inject(MatDialog);
  private router = inject(Router);
  private notification = inject(NotificationService);
  private enrollmentService = inject(EnrollmentService);
  username = this.api.getUsername();

  ngOnInit(): void {
    this.fetchCourses();

    this.enrollmentService.enrolledCourses$.subscribe((courses) => {
      this.enrolledCourses = courses;
    });
    this.enrollmentService.loadEnrolledCourses();
  }

  toggleEnrollment(courseId: number): void {
    if (this.isEnrolled(courseId)) {
      this.enrollmentService.unenroll(courseId);
    } else {
      this.enrollmentService.enroll(courseId);
    }
  }
    
  isEnrolled(courseId: number): boolean {
    return this.enrolledCourses.some((enrollment) => enrollment.course.courseId === courseId);
  }


  fetchCourses(): void {
    this.api.get('/courses').subscribe({
      next: (resp: any) => (this.courses = resp.data),
      error: () => this.notification.show('Failed to get all courses', true),
    });
  }

  openCourse(course?: any): void {
    const dialogRef = this.dialog.open(CourseModalComponent, {
      data: course || null,
      width: '400px',
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) this.fetchCourses();
    });
  }

  deleteCourse(courseId: number): void {
    if (confirm('Are you sure you want to delete this course?')) {
      this.api.delete(`/courses/${courseId}`).subscribe({
        next: () => {
          this.fetchCourses();
          this.notification.show('Course deleted successfully');
        },
        error: () => this.notification.show('Failed to delete course', true),
      });
    }
  }

  goToCourseDetails(courseId: number): void {
    this.router.navigate([`courses/${courseId}/modules`]);
  }
}
