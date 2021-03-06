
import React from 'react';
import {Modal, Button, Form} from 'react-bootstrap'
import { createCard } from '../api-js/api-interaction';

const eventTypeOptions = ['Back to School', 'Baptism and Christening', 'Baby', 'Bar/Bat Mitzvah', 'Birthday',
    'Confirmation', 'Congratulations', 'Encouragement', 'First Communion', 'Get Well', 'Graduation', 'Retirement', 'Sympathy', 
    'Teacher Appreciation', 'Thank You', 'Wedding']

function AddCard(props) {
    const [show, setShow] = React.useState(false)
    const [recipient, setRecipient] = React.useState('')
    const [eventType, setEventType] = React.useState('Anniversary')
    const [orientation, setOrientation] = React.useState('Landscape')

    const handleOpen = () => { 
        setEventType('Anniversary');
        setOrientation('Landscape');
        setShow(true);
    }
    const handleClose = () => { setShow(false) }

    const handleRecipientChange = (e) => { setRecipient(e.target.value) }
    const handleEventChange = (e) => { setEventType(e.target.value) } 
    const handleOrientationChange = (e) => { setOrientation(e.target.value) }

    const handleCreateCard = () => {
        let jsObj = {
            recipient: recipient,
            eventType: eventType,
            orientation: orientation
        }

        createCard(jsObj);
        handleClose();

    }

    return (
        <div className="AddCard">
            <Button variant="outline-light" onClick={handleOpen}>Add Card</Button>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Create A Card</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group controlId="form-recipient">
                            <Form.Label>Recipient</Form.Label>
                            <Form.Control required placeholder="Recipient" onChange={handleRecipientChange}/>
                        </Form.Group>
                        
                        <Form.Group controlId="form-event-type">
                            <Form.Label>Event Type</Form.Label>
                            <Form.Control as="select" onChange={handleEventChange}>
                                <option defaultValue>Anniversary</option>
                                {eventTypeOptions.map((eto) => {
                                    return (
                                        <option key={eto}>{eto}</option>
                                    )
                                })}
                            </Form.Control>
                        </Form.Group>

                        <Form.Group controlId="form-orientation">
                            <Form.Label>Card Orientation</Form.Label>
                            <Form.Control required as="select" onChange={handleOrientationChange}>
                                <option defaultValue>Landscape</option>
                                <option>Portrait</option>
                            </Form.Control>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant='danger' onClick={handleClose}>Cancel</Button>
                    <Button variant='success' onClick={handleCreateCard}>Create Card</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default AddCard;
