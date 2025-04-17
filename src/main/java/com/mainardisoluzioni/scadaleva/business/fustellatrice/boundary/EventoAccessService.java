/*
 * Copyright (C) 2025 maina
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mainardisoluzioni.scadaleva.business.fustellatrice.boundary;

import com.mainardisoluzioni.scadaleva.business.fustellatrice.entity.EventoAccess;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import java.lang.reflect.UndeclaredThrowableException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

/**
 *
 * @author maina
 */
@Stateless
public class EventoAccessService {
    @Resource(lookup="jdbc/access_readOnly_scadaleva_archivioStorico")
    DataSource dataSource;
    
    /*@PersistenceContext(unitName = "scadaleva_access_readOnly_archivioStorico_PU")
    EntityManager em;
    
    public List<EventoAccess> list(LocalDateTime limiteInferioreCreazione) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<EventoAccess> query = cb.createQuery(EventoAccess.class);
            Root<EventoAccess> root = query.from(EventoAccess.class);
            CriteriaQuery<EventoAccess> select = query.select(root).distinct(true);

            List<Predicate> conditions = new ArrayList<>();        
            conditions.add(cb.isNotNull(root.get(EventoAccess_.colpi)));
            conditions.add(cb.greaterThan(root.get(EventoAccess_.colpi), 0));
            if (limiteInferioreCreazione != null)
                conditions.add(cb.greaterThan(root.get(EventoAccess_.creazione), limiteInferioreCreazione));
            if (!conditions.isEmpty())
                query.where(conditions.toArray(Predicate[]::new));

            return em.createQuery(select).getResultList();
        } catch (java.lang.IllegalStateException e) {
            return Collections.emptyList();
        }
    }*/
    
    /**
     * Elenco degli eventi di Access
     * @param limiteInferioreCreazione se presente applica un filtro in modo che vengano restituiti tutti gli eventi la cui data di produzione è successiva (maggiore) a quanto indicato
     * @return Una lista degli eventi Access in cui il campo 'colpi' non è nullo ed è maggiore di zero
     */
    public List<EventoAccess> list(LocalDateTime limiteInferioreCreazione) {
        List<EventoAccess> result = new ArrayList<>();
        try {
            Connection connection = null;
            try {
                //connection = DriverManager.getConnection("jdbc:ucanaccess:///home/maina/scadaleva/fustellatrice/ArchivioStorico.Mdb");
                connection = dataSource.getConnection();
                
                StringBuilder testoQuery = new StringBuilder("SELECT * FROM Ingressi ");
                List<StringBuilder> conditions = new ArrayList<>();
                conditions.add(new StringBuilder(" colpi IS NOT NULL "));
                conditions.add(new StringBuilder(" colpi > 0 "));
                if (limiteInferioreCreazione != null)
                    conditions.add(new StringBuilder(" `Ora Inizio` > ? "));

                testoQuery = testoQuery.append(" WHERE ");
                for (int i = 0; i < conditions.size(); i++) {
                    if (i > 0)
                        testoQuery = testoQuery.append("AND ");
                    testoQuery = testoQuery.append(conditions.get(i));
                }
                
                testoQuery.append(";");

                PreparedStatement prepareStatement = connection.prepareStatement(testoQuery.toString());
                if (limiteInferioreCreazione != null)
                    prepareStatement.setTimestamp(1, Timestamp.valueOf(limiteInferioreCreazione));
                ResultSet rs = prepareStatement.executeQuery();
                while (rs.next()) {
                    EventoAccess eventoAccess = new EventoAccess();

                    eventoAccess.setCodiceRicettaAccess(rs.getString("Nome programma"));
                    eventoAccess.setColpi(rs.getInt("colpi"));
                    eventoAccess.setCreazione(rs.getTimestamp("Ora Inizio").toLocalDateTime());
                    eventoAccess.setTermine(rs.getTimestamp("Ora Fine").toLocalDateTime());
                    eventoAccess.setTipo(rs.getString("tipo"));

                    result.add(eventoAccess);
                }
            } catch (Exception e) {
                // niente da fare
                System.err.println("Errore: " + e.getLocalizedMessage());
            } finally {
                if (connection != null)
                    connection.close();
            }
        } catch (UndeclaredThrowableException | SQLException e) {
            // niente da fare
        }
        
        /*try (Connection connection = dataSource.getConnection()) {
            StringBuilder testoQuery = new StringBuilder("SELECT ea FROM Ingressi ea ");
            List<StringBuilder> conditions = new ArrayList<>();
            conditions.add(new StringBuilder(" NOT colpi IS NULL "));
            conditions.add(new StringBuilder(" colpi > 0 "));
            if (limiteInferioreCreazione != null)
                conditions.add(new StringBuilder(" `Ora Inizio` > ? "));

            testoQuery = testoQuery.append(" WHERE ");
            for (int i = 0; i < conditions.size(); i++) {
                if (i > 0)
                    testoQuery = testoQuery.append("AND ");
                testoQuery = testoQuery.append(conditions.get(i));
            }

            PreparedStatement prepareStatement = connection.prepareStatement(testoQuery.toString());
            if (limiteInferioreCreazione != null)
                prepareStatement.setTimestamp(1, Timestamp.valueOf(limiteInferioreCreazione));
            ResultSet rs = prepareStatement.executeQuery();
            while (rs.next()) {
                EventoAccess eventoAccess = new EventoAccess();

                eventoAccess.setCodiceRicettaAccess(rs.getString("`Nome programma`"));
                eventoAccess.setColpi(rs.getInt("colpi"));
                eventoAccess.setCreazione(rs.getTimestamp("`Ora Inizio`").toLocalDateTime());
                eventoAccess.setTermine(rs.getTimestamp("`Ora Fine`").toLocalDateTime());
                eventoAccess.setTipo(rs.getString("tipo"));

                result.add(eventoAccess);
            }
        } catch (SQLException e) {
            // niente da fare
        }*/
        
        return result;
    }
    
}
