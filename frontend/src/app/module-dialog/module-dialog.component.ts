import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { ApiService } from '../api.service';
import { NotificationService } from '../notification.service';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-module-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    FormsModule,
  ],
  templateUrl: './module-dialog.component.html',
  styleUrls: ['./module-dialog.component.scss'],
})
export class ModuleDialogComponent {
  module = { name: '', courseId: '' };
  isEdit: boolean = false;

  constructor(
    private dialogRef: MatDialogRef<ModuleDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: { module?: any; courseId: string },
    private apiService: ApiService,
    private notification: NotificationService
  ) {
    if (data.module) {
      this.isEdit = true;
      this.module = { ...data.module };
    }
    this.module.courseId = data.courseId;
  }

  save(): void {
    const method = this.isEdit ? 'put' : 'post';

    this.apiService[method]('/modules', this.module).subscribe({
      next: () => this.dialogRef.close(true),
      error: (err) => {
        this.notification.show('Failed to save module', true);
        this.dialogRef.close(false);
      },
    });
  }
}
