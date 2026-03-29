import mongoose, { Schema, model } from 'mongoose';

const ratingSchema = new Schema({
    rating: {
        type: Number,
        required: true,
        min: 1,
        max: 5,
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

export const ratingModel = model('Rating', ratingSchema);
