import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { ApiService } from './api.service';
import { NotificationService } from './notification.service';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {
  private enrolledCourses = new BehaviorSubject<any[]>([]);
  enrolledCourses$ = this.enrolledCourses.asObservable();

  constructor(private api: ApiService, private notification: NotificationService) {}

  // Fetch all enrolled courses
  loadEnrolledCourses(): void {
    this.api.get('/enroll').subscribe({
      next: (resp: any) => this.enrolledCourses.next(resp.data),
      error: () => this.notification.show('Failed to load enrolled courses.', true),
    });
  }

  // Enroll in a course
  enroll(courseId: number): void {
    this.api.post('/enroll', { courseId }).subscribe({
      next: () => {
        this.notification.show('Successfully enrolled in course!', false);
        this.loadEnrolledCourses(); // Refresh list
      },
      error: () => this.notification.show('Failed to enroll in course.', true),
    });
  }

  // Unenroll from a course
  unenroll(courseId: number): void {
    this.api.delete(`/enroll?courseId=${courseId}`).subscribe({
      next: () => {
        this.notification.show('Successfully unenrolled from course!', false);
        this.loadEnrolledCourses(); // Refresh list
      },
      error: () => this.notification.show('Failed to unenroll from course.', true),
    });
  }
}
