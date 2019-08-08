/**
 * Database migration script for Riff's PostgreSQL database.
 *
 * This script renames the study names stored in the queries for the cohort builder and the file repository.
 * This is done so that the former queries remain valid once the studies have been renamed in the release coordinator.
 * 
 * @date 2019-08-07
 */

/*
 * The search queries in the cohort builder are stored as JSON.
 * We include the initial double quote, to reduce risks of false positives.
 */
UPDATE riff SET content = REPLACE(content::TEXT, '"Consortium: Pediatric Brain Tumors - CBTTC', '"Pediatric Brain Tumor Atlas: CBTTC')::JSON;

/*
 * The search queries in the file repository are stored as their full URL. The SQON is therefore URL-encoded:
 * We include the initial double quote, to reduce risks of false positives.
 */
UPDATE riff SET content = REPLACE(content::TEXT, '%22Consortium3A%20Pediatric%20Brain%20Tumors%20-%20CBTTC', '%22Pediatric%20Brain%20Tumors%20Atlas%3A%20CBTTC')::JSON;

