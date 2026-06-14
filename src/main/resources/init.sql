-- ============================================
-- TABLA: carrera
-- ============================================
CREATE TABLE IF NOT EXISTS carrera (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL UNIQUE,
    categoria VARCHAR(100) NOT NULL,
    descripcion TEXT,
    nota_minima_recomendada VARCHAR(50),
    influencer_nombre VARCHAR(200),
    influencer_plataforma VARCHAR(100) DEFAULT 'YouTube',
    influencer_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- TABLA: beneficio
-- ============================================
CREATE TABLE IF NOT EXISTS beneficio (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(200) NOT NULL UNIQUE,
    descripcion TEXT,
    condiciones JSONB,
    requisitos JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- DATOS: carreras
-- ============================================
INSERT INTO carrera (nombre, categoria, descripcion, nota_minima_recomendada, influencer_nombre, influencer_url) VALUES
('Ingeniería Aeronáutica', 'Ingeniería', 'Diseño, mantenimiento y operación de aeronaves y sistemas aeroespaciales.', '14', 'SergioHidalgoAero', 'https://www.youtube.com/@SergioHidalgoAero'),
('Ingeniería Automotriz', 'Ingeniería', 'Diseño, fabricación y mantenimiento de vehículos automotores y sus sistemas.', '14', 'LaTalacha', 'https://www.youtube.com/@LaTalachaPodcast1'),
('Ingeniería Biomédica', 'Ingeniería', 'Aplicación de la ingeniería al campo médico: equipos, prótesis, diagnóstico por imágenes.', '14', 'Biomedex Ingeniería Biomédica', 'https://www.youtube.com/@biomedexingenieriabiomedic6248'),
('Ingeniería Civil', 'Ingeniería', 'Diseño, construcción y mantenimiento de infraestructuras: edificios, puentes, carreteras.', '14', 'IngenieroCiviltucanalfavorito', 'https://www.youtube.com/c/ingenierociviltucanalfavorito'),
('Ingeniería Electrónica', 'Ingeniería', 'Diseño de circuitos, sistemas de control, telecomunicaciones y dispositivos electrónicos.', '14', 'The Professor Garcia', 'https://www.youtube.com/user/elprofegarcia'),
('Ingeniería Eléctrica y de Potencia', 'Ingeniería', 'Generación, transmisión y distribución de energía eléctrica a gran escala.', '14', 'Sígueme la Corriente', 'https://www.youtube.com/@SiguemeLaCorriente'),
('Ingeniería Empresarial', 'Ingeniería', 'Gestión de procesos empresariales integrando conocimientos de ingeniería y administración.', '13', 'EmprendeAprendiendo', 'https://www.youtube.com/@EmprendeAprendiendo'),
('Ingeniería Industrial', 'Ingeniería', 'Optimización de procesos productivos, logística y gestión de la cadena de suministro.', '14', 'Ingeniería Industrial Easy', 'https://www.youtube.com/@ingenieriaIndustrialeasy'),
('Ingeniería Mecatrónica', 'Ingeniería', 'Integración de mecánica, electrónica y computación para crear sistemas automatizados.', '14', 'EstudiaConMarisol', 'https://www.youtube.com/@EstudiaConMarisol'),
('Ingeniería Mecánica', 'Ingeniería', 'Diseño, fabricación y mantenimiento de máquinas, sistemas mecánicos y térmicos.', '14', 'El Traductor de Ingeniería', 'https://www.youtube.com/@eltraductor_ok'),
('Ingeniería de Minas', 'Ingeniería', 'Exploración, extracción y procesamiento de recursos minerales de forma sostenible.', '14', 'TELEVISION MINING COURSE', 'https://www.youtube.com/@rumbominerotelevision7898'),
('Ingeniería de Seguridad Industrial y Minera', 'Ingeniería', 'Prevención de riesgos laborales y gestión de seguridad en entornos industriales y mineros.', '14', 'HSE Ingeniería', 'https://www.youtube.com/@hseingenieria350'),
('Ingeniería de Sistemas e Informática', 'Ingeniería', 'Desarrollo y gestión de sistemas de información, redes y tecnologías de la información.', '14', 'Juan Villalvazo', 'https://www.youtube.com/@vidaprogramador'),
('Ingeniería de Software', 'Ingeniería', 'Diseño, desarrollo y mantenimiento de aplicaciones y sistemas de software.', '14', 'MoureDev by Brais Moure', 'https://www.youtube.com/@mouredev'),
('Ingeniería de Telecomunicaciones', 'Ingeniería', 'Diseño y gestión de redes de comunicación: fibra óptica, satélites, redes móviles.', '14', 'Destino Profesional', 'https://www.youtube.com/@DestinoProfesional'),
('Ingeniería Ambiental', 'Ingeniería', 'Protección del medio ambiente, gestión de residuos y desarrollo sostenible.', '14', 'AmbientalMente S.A.S.', 'https://www.youtube.com/@ambientalmentes.a.s.4466'),
('Administración Banca y Finanzas', 'Negocios', 'Gestión financiera, análisis de inversiones y administración bancaria.', '12', 'Invertir Mejor', 'https://www.youtube.com/@invertirmejoronline'),
('Administración de Empresas', 'Negocios', 'Dirección, organización y gestión de empresas de cualquier rubro.', '12', '100 negocios', 'https://www.youtube.com/@100negocios'),
('Administración Hotelera y de Turismo', 'Negocios', 'Gestión de hoteles, restaurantes y servicios turísticos.', '12', 'TOURISTEANDO', 'https://www.youtube.com/@touristeandoBB'),
('Administración de Negocios Internacionales', 'Negocios', 'Gestión del comercio exterior y negocios globales.', '12', 'VisualPolitik', 'https://www.youtube.com/@VisualPolitik'),
('Administración y Marketing', 'Negocios', 'Estrategias de mercado, publicidad y gestión de marcas.', '12', 'Vilma OS', 'https://www.youtube.com/@VilmaNunez'),
('Contabilidad', 'Negocios', 'Registro, análisis y control financiero de organizaciones.', '13', 'TUTO CONTABLE', 'https://www.youtube.com/@TUTOCONTABLE'),
('Economía', 'Negocios', 'Análisis de mercados, políticas económicas y desarrollo financiero.', '14', 'VisualEconomik', 'https://www.youtube.com/@VisualEconomik'),
('Derecho', 'Derecho', 'Estudio del sistema legal, defensa de derechos y asesoría jurídica.', '13', 'Justicia Transparente', 'https://www.youtube.com/@licenciadotransparente'),
('Educación inicial', 'Educación', 'Formación y desarrollo integral de niños en edad preescolar.', '13', 'Mundo Inicial', 'https://www.youtube.com/@mundoinicial'),
('Educación Primaria', 'Educación', 'Enseñanza y formación académica de niños en nivel primario.', '13', 'Aula PT Primaria', 'https://www.youtube.com/@aulaptprimaria9936'),
('Psicología', 'Psicología', 'Estudio del comportamiento humano y la salud mental.', '13', 'Psych2Go Español', 'https://www.youtube.com/@P2Go'),
('Ciencias de la Comunicación', 'Comunicaciones', 'Análisis y producción de medios de comunicación y periodismo.', '13', 'Luisito Comunica', 'https://www.youtube.com/luisitocomunica'),
('Comunicación y Publicidad', 'Comunicaciones', 'Creación de estrategias publicitarias y campañas comunicacionales.', '13', 'Roberto Pérez', 'https://www.youtube.com/@RobertoPerezMultinivel'),
('Diseño Digital Publicitario', 'Comunicaciones', 'Diseño de contenido visual y digital para campañas publicitarias.', '13', 'Anson Alexander', 'https://www.youtube.com/@AnsonAlexander'),
('Diseño Profesional Gráfico', 'Comunicaciones', 'Creación de identidad visual, diseño editorial y branding.', '13', 'Hey Jaime', 'https://www.youtube.com/@HeyJaime'),
('Arquitectura', 'Arquitectura', 'Diseño y planificación de espacios arquitectónicos y urbanos.', '13', 'Architecture tutorials.', 'https://www.youtube.com/@Tutorialesdearquitectura'),
('Diseño Profesional de Interiores', 'Arquitectura', 'Diseño y decoración de espacios interiores funcionales y estéticos.', '13', 'CREDesigners', 'https://www.youtube.com/@credesigners-cristinalopez5628/shorts'),
('Enfermería', 'Salud', 'Cuidado integral de pacientes y asistencia en procedimientos médicos.', '14', 'Enfermero Jorge Angel', 'https://www.youtube.com/@enfermerojorgeangel'),
('Nutrición y Dietética', 'Salud', 'Evaluación nutricional y diseño de planes alimenticios para la salud.', '14', 'Juan Revenga', 'https://www.youtube.com/channel/UC7VWR82Rm069yUSjuloUH7Q'),
('Obstetricia', 'Salud', 'Atención integral de la mujer durante el embarazo, parto y puerperio.', '14', 'Arancha Matrona', 'https://www.youtube.com/@AranchaMatrona'),
('Tecnología Médica en Terapia Física y Rehabilitación', 'Salud', 'Evaluación y tratamiento de trastornos del movimiento y la función física.', '14', 'FisioOnline', 'https://www.youtube.com/@FisioOnline'),
('Odontología', 'Salud', 'Prevención, diagnóstico y tratamiento de enfermedades bucodentales.', '14', 'Dr. Daniel Avila', 'https://www.youtube.com/@dr.danielavila'),
('Farmacia y Bioquímica', 'Salud', 'Desarrollo y control de medicamentos, análisis clínicos y bioquímicos.', '14', 'UCSMAREQUIPA', 'https://www.youtube.com/@UCSMAREQUIPA'),
('Tecnología Médica en Laboratorio Clínico y Anatomía Patológica', 'Salud', 'Análisis de muestras biológicas para diagnóstico de enfermedades.', '14', 'Laboratorio Clínico UNAM', 'https://www.youtube.com/@UCSMAREQUIPA'),
('Medicina', 'Medicina', 'Diagnóstico, tratamiento y prevención de enfermedades en seres humanos.', '15', 'ElEternoEstudiantedeMedicina', 'https://www.youtube.com/c/eleternoestudiantedemedicina')
ON CONFLICT (nombre) DO NOTHING;

-- ============================================
-- DATOS: beneficios
-- ============================================
INSERT INTO beneficio (nombre, descripcion, condiciones, requisitos) VALUES
(
    'Tercio Superior',
    'Para estudiantes que pertenecieron al tercio superior en 4° y 5° de secundaria',
    '{"anios": [4, 5], "promedio_minimo": 16}',
    '["Ficha de inscripción", "Fotocopia DNI", "Constancia de pago", "Constancia de tercio superior", "Carta original del colegio"]'
),
(
    'Buen Rendimiento Escolar',
    'Promedio de 14 o más en 4° y 5° de secundaria',
    '{"anios": [4, 5], "promedio_minimo": 14}',
    '["Ficha de inscripción", "Fotocopia DNI", "Constancia de pago", "Carta del colegio con promedio de 14+"]'
),
(
    'Examen Preferencial',
    'Para estudiantes que egresaron el año anterior',
    '{"diferencia_anios": 1}',
    '["Ficha de inscripción", "Fotocopia DNI", "Constancia de pago", "Certificado de estudios 1° a 5°"]'
),
(
    'Examen Regular',
    'Para estudiantes que egresaron hace 2 o más años',
    '{"diferencia_anios": ">=2"}',
    '["Ficha de inscripción", "Fotocopia DNI", "Constancia de pago", "Certificado de estudios 1° a 5°"]'
)
ON CONFLICT (nombre) DO NOTHING;
