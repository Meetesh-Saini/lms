import { Component, Inject, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { NotificationService } from '../notification.service';

@Component({
  selector: 'app-course-modal',
  standalone: true,
  imports: [ReactiveFormsModule, MatInputModule, MatButtonModule, MatSelectModule, MatTabsModule, MatCardModule, FormsModule],
  templateUrl: './course-modal.component.html',
  styleUrls: ['./course-modal.component.scss'],
})
export class CourseModalComponent {
  private api = inject(ApiService);
  public dialogRef = inject(MatDialogRef<CourseModalComponent>);
  public course;
  notification: NotificationService;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private fb: FormBuilder, notification: NotificationService) {
    this.course = {
      title: data?.title || '',
      description: data?.description || '',
      category: data?.category || '',
      status: data?.status || '',
    }

    this.notification = notification;
  }

  saveCourse(): void {
    const saveAction = this.data
      ? this.api.put(`/courses/${this.data.courseId}`, this.course)
      : this.api.post('/courses', this.course);

    saveAction.subscribe({
      next: () => this.dialogRef.close(true),
      error: () => { this.notification.show("Failed to save course", true) },
    });
  }
}
