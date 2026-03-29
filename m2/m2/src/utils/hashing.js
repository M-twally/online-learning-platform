import bcrypt from "bcryptjs";

export const hash = (text) => {
  const hashResult = bcrypt.hashSync(text, +process.env.SALT_ROUND);
  return hashResult;
};

export const compare = (text, hashValue) => {
  const match = bcrypt.compareSync(text, hashValue);
  return match;
};
