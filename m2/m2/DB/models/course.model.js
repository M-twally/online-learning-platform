import mongoose, { Schema, model } from 'mongoose';

const courseSchema = new Schema({
    courseName: {
        type: String,
        required: true,
        trim: true,
        lowercase: true, // Convert the course name to lowercase
    },
    duration: {
        type: Number,
        required: true,
    },
    category: {
        type: String,
        required: true,
        trim: true,
    },
    capacity: {
        type: Number,
        required: true,
    },
    enrolledStudents: {
        type: Number,
        default: 0,
    },
    user: {
        userId: {
            type: String, // Assuming userId is a string
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
    published: {
        type: Boolean,
        default: false, // Default value set to false
    },
}, {
    timestamps: true,
});



export const courseModel = model('Course', courseSchema);
