import mongoose from 'mongoose';

const enrollmentSchema = new mongoose.Schema({
  courseName: {
    type: String,
    required: true
  },
  category: {
    type: String,
    required: true
  },
  instructorId: {
    type: String,
    required: true
  },
  instructorName: {
    type: String,
    required: true
  },
  status: {
    type: String,
    default: 'pending'
  },
  courseId: {
    type: String,
    required: true
  },
  studentName: {
    type: String,
    required: true
  },
  studentEmail: {
    type: String,
    required: true
  }
}, {
  timestamps: true
});

export const enrollmentModel = mongoose.model('Enrollment', enrollmentSchema);
