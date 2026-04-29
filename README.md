# Rippling Mini — Demo

A mini version of Rippling's core feature: add an employee, and Gmail, Slack, and Payroll get provisioned automatically.

Built for the Rippling SWE interview.

---

## What it does

1. You fill out a form with an employee's name, email, department, and role
2. Hit submit — the backend creates the employee and instantly spins up 3 app accounts
3. You see the employee in a table with Gmail / Slack / Payroll badges
4. Click any employee to open a modal — mark each app as done, see the audit log

That's it. Simple, but it shows the core Rippling idea: one action triggers a workflow.

---

<img width="1619" height="907" alt="Screenshot 2026-04-29 130655" src="https://github.com/user-attachments/assets/c0f42a71-e0c3-47e4-8399-b078d46c456e" />


<img width="1546" height="575" alt="Screenshot 2026-04-29 130704" src="https://github.com/user-attachments/assets/2b45d3b9-e5e2-4c49-8051-b4f2e3d2f10f" />


<img width="1205" height="865" alt="Screenshot 2026-04-29 132303" src="https://github.com/user-attachments/assets/581af9ae-7008-4610-8d1f-f2014ba2e5be" />


---
## Tech

- **Backend** — Spring Boot 3, Java 17, H2 in-memory DB, JPA
- **Frontend** — React + Vite, plain CSS, Axios

---

## Run it (2 commands)

**Terminal 1 — Backend**
```bash
cd rippling-demo
.\mvnw.cmd spring-boot:run
```

**Terminal 2 — Frontend**
```bash
cd frontend
npm run dev
```

Open http://localhost:5173

The app starts with 5 demo employees already loaded. No setup, no config.

---

## Endpoints (if you want to poke around)

| Method | URL | What it does |
|--------|-----|--------------|
| POST | /api/employees | Add employee + auto-provision |
| GET | /api/employees | List all employees |
| GET | /api/provisions/employee/:id | Get app provisions for employee |
| POST | /api/provisions/:id/complete | Mark a provision done |
| GET | /api/provisions/logs/employee/:id | Get audit log |

H2 console → http://localhost:8080/h2-console
JDBC URL: `jdbc:h2:mem:ripplingdb` · Username: `sa` · Password: *(blank)*
