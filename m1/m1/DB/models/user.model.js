import mongoose, { Schema, model } from 'mongoose';
// import bcrypt from "bcrypt"

const userSchema = new Schema({
    name: {
        type: String,
        required: true,
        trim: true,
    },
    email: {
        type: String,
        required: true,
        unique: true,
        trim: true,
    },
    password: {
        type: String,
        required: true,
    },
    // Common fields for all roles
    role: {
        type: String,
        enum: ['admin', 'instructor', 'student'],
        required: true,
    },
    // Fields specific to the Instructor role
    affiliation: String,
    yearsOfExperience: Number,
    bio: String,
}, {
    timestamps: true,
});

// userSchema.pre(`save`,function(){
//     this.password=bcrypt.hashSync(this.password,parseInt(process.env.SALT_ROUND))
    
// })

export const userModel = model('User', userSchema);
