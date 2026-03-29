import mongoose, { Schema, model } from 'mongoose';

const reviewSchema = new Schema({
    reviewText: {
        type: String,
        required: true,
        trim: true,
    },
    user: {
        userId: {
            type: String,
            required: true,
        },
        userName: {
            type: String,
            required: true,
        },
        userEmail: {
            type: String,
            required: true,
        },
    },
    course: {
        courseId: {
            type: String,
            required: true,
        },
        courseName: {
            type: String,
            required: true,
        },
        instructorName: {
            type: String,
            required: true,
        },
    },
}, {
    timestamps: true,
});

export const reviewModel = model('Review', reviewSchema);
