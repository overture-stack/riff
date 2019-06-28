/**
 * Database migration script for Riff's PostgreSQL database.
 *
 * This script renames the study names stored in the queries for the cohort builder and the file repository.
 * This is done so that the former queries remain valid once the studies have been renamed in the release coordinator.
 * 
 * @date 2019-06-28
 */

/*
 * The search queries in the cohort builder are stored as JSON.
 * The elements in this list are alphatically sorted.
 * We include the initial double quote, to reduce risks of false positives.
 */
UPDATE riff SET content = REPLACE(content::TEXT, '"Adolescent Idiopathic Scoliosis', '"Kids First: Adolescent Idiopathic Scoliosis')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Congenital Diaphragmatic Hernia', '"Kids First: Congenital Diaphragmatic Hernia')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Congenital Hearth Defects', '"Kids First: Congenital Hearth Defects')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Disorder of Sex Development', '"Kids First: Disorder of Sex Development')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Ewing Sarcoma: Genetic Risk', '"Kids First: Ewing Sarcoma - Genetic Risk')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Orofacial Cleft: European Ancestry', '"Kids First: Orofacial Cleft: European Ancestry')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Orofacial Cleft: Latin American', '"Kids First: Orofacial Cleft: Latin American')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Pediatric Brain Tumor: CBTTC', '"Consortium: Pediatric Brain Tumor - CBTTC')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"Syndromic Cranial Dysinnervation', '"Kids First: Syndromic Cranial Dysinnervation')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"TARGET: Acute Myeloid Leukemia', '"NCI CRDC: TARGET - Acute Myeloid Leukemia')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '"TARGET: Neuroblastoma', '"NCI CRDC: TARGET - Neuroblastoma')::JSON;

/*
 * The search queries in the file repository are stored as their full URL. The SQON is therefore URL-encoded:
 * The elements in this list are alphatically sorted.
 * We include the initial double quote, to reduce risks of false positives.
 */
UPDATE riff SET content = REPLACE(content::TEXT, '%22Adolescent%20Idiopathic%20Scoliosis', '%22Kids%20First%3A%20Adolescent%20Idiopathic%20Scoliosis')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Congenital%20Diaphragmatic%20Hernia', '%22Kids%20First%3A%20Congenital%20Diaphragmatic%20Hernia')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Congenital%20Hearth%20Defects', '%22Kids%20First%3A%20Congenital%20Hearth%20Defects')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Disorder%20of%20Sex%20Development', '%22Kids%20First%3A%20Disorder%20of%20Sex%20Development')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Ewing%20Sarcoma%3A%20Genetic%20Risk', '%22Kids%20First%3A%20Ewing%20Sarcoma%20-%20Genetic%20Risk')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Orofacial%20Cleft:%20European%20Ancestry', '%22Kids%20First%3A%20Orofacial%20Cleft:%20European%20Ancestry')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Orofacial%20Cleft:%20Latin%20American', '%22Kids%20First%3A%20Orofacial%20Cleft:%20Latin%20American')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Pediatric%20Brain%20Tumors%3A%20CBTTC', '%22Consortium3A%20Pediatric%20Brain%20Tumors%20-%20CBTTC')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22Syndromic%20Cranial%20Dysinnervation', '%22Kids%20First%3A%20Syndromic%20Cranial%20Dysinnervation')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22TARGET%3A%20Acute%20Myeloid%20Leukemia', '%22NCI%20CRDC%3A%20TARGET%20-%20Acute%20Myeloid%20Leukemia')::JSON;
UPDATE riff SET content = REPLACE(content::TEXT, '%22TARGET%3A%20Neuroblastoma', '%22NCI%20CRDC%3A%20TARGET%20-%20Neuroblastoma')::JSON;

