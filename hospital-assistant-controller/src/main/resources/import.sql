-- ROLES
INSERT INTO roles
  (name)
VALUES
  ('ROLE_PATIENT'),
  ('ROLE_SANITARY'),
  ('ROLE_NURSE'),
  ('ROLE_ADMIN'),
  ('ROLE_DOCTOR');

-- INTENTS
INSERT INTO intents(name, message, priority, role_id)
SELECT 'BANDAGE_INTENT', 'Стая №1. Помощ с превръзката', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'BLOOD_PRESSURE_INTENT', 'Стая №1. Измерване на кръвно', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'CATHETER_INTENT', 'Стая №1. Преглед на катетър', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'HURTS_INTENT', 'Стая №1. Болкоуспокояващи', 'HIGH', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'MEDICAL_DRIP_BAG_INTENT', 'Стая №1. Проблем с банката', 'HIGH', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'TEMPERATURE_INTENT', 'Стая №1. Измерване на температура', 'HIGH', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'WET_UNDER_HAND_INTENT', 'Стая №1. Мокро под ръката', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_NURSE';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'BATHROOM_INTENT', 'Стая №1. Помощ в банята', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_SANITARY';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'PEE_INTENT', 'Стая №1. Малка нужда', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_SANITARY';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'DIAPER_INTENT', 'Стая №1. Смяна на памперс', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_SANITARY';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'POOP_INTENT', 'Стая №1. Голяма нужда', 'MODERATE', roles.id
FROM roles
WHERE name = 'ROLE_SANITARY';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'UTILITY_INTENT', 'Стая №1. Помощ за нещо в стаята', 'LOW', roles.id
FROM roles
WHERE name = 'ROLE_SANITARY';

INSERT INTO intents(name, message, priority, role_id)
SELECT 'EMERGENCY_INTENT', 'Стая №1. Спешна помощ', 'HIGH', roles.id
FROM roles
WHERE name = 'ROLE_DOCTOR';

--USERS
--NURSES
INSERT INTO users(email, password)
VALUES('nurse1@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_NURSE' AND users.email = 'nurse1@abv.bg';

INSERT INTO users(email, password)
VALUES('nurse2@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_NURSE' AND users.email = 'nurse2@abv.bg';

INSERT INTO users(email, password)
VALUES('nurse3@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_NURSE' AND users.email = 'nurse3@abv.bg';

--SANITARIES
INSERT INTO users(email, password)
VALUES('sanitary1@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_SANITARY' AND users.email = 'sanitary1@abv.bg';

--PATIENTS
INSERT INTO users(email, password)
VALUES('patient1@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_PATIENT' AND users.email = 'patient1@abv.bg';

--GOOGLE HOME MINI
INSERT INTO users(email, password)
VALUES('google_home_mini@abv.bg', '$2a$10$7OKMxAS1wn/9o4Q86hXSbuHTJI2cMHNoBoTvpomacKmXxM47Iru3e');

INSERT INTO user_roles(user_id, role_id)
SELECT users.id, roles.id
FROM roles
JOIN users
WHERE roles.name = 'ROLE_ADMIN' AND users.email = 'google_home_mini@abv.bg';

--WORK SCHEDULE
INSERT INTO schedules(start, end, user_id)
SELECT "2019-12-13T10:15:30", "2020-03-13T10:15:30", id
FROM users
WHERE users.email = 'nurse1@abv.bg';

INSERT INTO schedules(start, end, user_id)
SELECT "2019-12-13T10:15:30", "2020-03-13T10:15:30", id
FROM users
WHERE users.email = 'nurse2@abv.bg';

INSERT INTO schedules(start, end, user_id)
SELECT "2019-12-13T10:15:30", "2020-03-13T10:15:30", id
FROM users
WHERE users.email = 'nurse3@abv.bg';

INSERT INTO schedules(start, end, user_id)
SELECT "2019-12-13T10:15:30", "2020-03-13T10:15:30", id
FROM users
WHERE users.email = 'sanitary1@abv.bg';