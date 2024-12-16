select * from locais;

SELECT ST_IsValid(ST_GeomFromText('POLYGON((-46.641 -23.551, -46.641 -23.539, -46.629 -23.539, -46.629 -23.551, -46.641 -23.551))'));


SELECT id, nome, ST_AsText(coordenadas) FROM locais;

SELECT 
    ST_Distance(
        ST_GeomFromText('POINT(-78.6400 -90.5450)', 4326), -- Coordenadas do "Cabaré"
        ST_MakeLine(
            ST_SetSRID(ST_MakePoint(-46.6333, -23.5505), 4326), -- Primeiro ponto extremo
            ST_SetSRID(ST_MakePoint(-46.6400, -23.5450), 4326)  -- Segundo ponto extremo
        )
    ) AS distancia_metros;

