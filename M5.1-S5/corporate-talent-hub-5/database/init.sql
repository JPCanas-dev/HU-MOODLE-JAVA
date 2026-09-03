CREATE TABLE empleado (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad SMALLINT NOT NULL CHECK (edad BETWEEN 18 AND 100),
    salario NUMERIC(12, 2) NOT NULL CHECK (salario > 0),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('DESARROLLADOR', 'GERENTE')),
    lenguaje_principal VARCHAR(50),
    presupuesto_mensual NUMERIC(12, 2),
    promedio_desempenio NUMERIC(5, 2) DEFAULT 0 CHECK (promedio_desempenio BETWEEN 0 AND 100),
    CHECK (
        (tipo = 'DESARROLLADOR' AND lenguaje_principal IS NOT NULL AND presupuesto_mensual IS NULL)
        OR
        (tipo = 'GERENTE' AND presupuesto_mensual IS NOT NULL AND lenguaje_principal IS NULL)
    )
);

CREATE TABLE calificacion (
    id SERIAL PRIMARY KEY,
    empleado_id INTEGER NOT NULL,
    trimestre SMALLINT NOT NULL CHECK (trimestre BETWEEN 1 AND 3),
    calificacion NUMERIC(5, 2) NOT NULL CHECK (calificacion BETWEEN 0 AND 100),
    CONSTRAINT fk_calificacion_empleado
        FOREIGN KEY (empleado_id)
        REFERENCES empleado(id)
        ON DELETE CASCADE,
    CONSTRAINT uq_empleado_trimestre
        UNIQUE (empleado_id, trimestre)
);